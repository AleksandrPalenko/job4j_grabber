package ru.job4j.grabber;
/**
 * класс описывает работу с планировщиком, чтением и записью данных с сайта
 */

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;
import ru.job4j.html.SqlRuParse;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class Grabber implements Grab {
    private final Properties cfg = new Properties();

    /**
     * метод описывает соединение чтение файла с настройками
     * @return на выходе настройки
     * @throws SQLException исключение ловим
     */
    public Store store() {
        return new PsqlStore(cfg);
    }

    /**
     * метод описывает планировщика
     * @return на выходе работающий планировщик
     * @throws SchedulerException исключения ловим
     */
    public Scheduler scheduler() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        return scheduler;
    }

    /**
     * метод описывает чтение файла с настройками
     * @throws IOException ловим исключения, которые могут появиться
     */
    public void cfg() throws IOException {
        try (InputStream in = new FileInputStream(new File("src/main/resources/app.properties"))) {
            cfg.load(in);
        }
    }

    /**
     * инициализаци задания
     * @param parse задание - парсер сайта
     * @param store задание - добавление в бд
     * @param scheduler расписание
     * @throws SchedulerException исключения ловим
     */
    @Override
    public void init(Parse parse, Store store, Scheduler scheduler) throws Exception {
            JobDataMap data = new JobDataMap();
            data.put("store", store);
            data.put("parse", parse);
            JobDetail job = newJob(GrabJob.class)
                    .usingJobData(data)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(Integer.parseInt(cfg.getProperty("time")))
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
    }

    public static class GrabJob implements Job {

        /**
         * метод описывает выполнение самого задания
         * @param context на входе контекст
         * @throws JobExecutionException исключения ловим
         */
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            JobDataMap map = context.getJobDetail().getJobDataMap();
            Store store = (Store) map.get("store");
            Parse parse = (Parse) map.get("parse");
            String url = "https://www.sql.ru/forum/job-offers";
            List<Post> post = null;
            try {
                post = parse.list(url);
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
            for (Post value : post) {
                store.save(value);
            }
        }
    }

    public void web(Store store) {
        new Thread(() -> {
            try (ServerSocket server = new ServerSocket(Integer.parseInt(cfg.getProperty("port")))) {
                while (!server.isClosed()) {
                    Socket socket = server.accept();
                    try (OutputStream out = socket.getOutputStream()) {
                        out.write("HTTP/1.1 200 OK\r\n\r\n".getBytes());
                        for (Post post : store.getAll()) {
                            out.write(post.toString().getBytes(Charset.forName("Windows-1251")));
                            out.write(System.lineSeparator().getBytes());
                        }
                    } catch (IOException io) {
                        io.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) throws Exception {
        Grabber grab = new Grabber();
        grab.cfg();
        Scheduler scheduler = grab.scheduler();
        Store store = grab.store();
        grab.init(new SqlRuParse(new SqlRuDateTimeParser()), store, scheduler);
        grab.web(store);
    }
}