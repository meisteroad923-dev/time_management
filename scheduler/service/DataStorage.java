package scheduler.service;

import java.io.*;

/**
 * ScheduleManager의 데이터를 파일에 저장하고
 * 다시 불러오는 기능을 제공한다.
 */
public class DataStorage {

    private static final String FILE_NAME = "scheduleData.dat";


    /**
     * 현재 일정 데이터를 파일에 저장한다.
     *
     * @param scheduleManager 저장할 일정 관리자
     */
    public static void save(ScheduleManager scheduleManager) {

        try (
            ObjectOutputStream output =
                new ObjectOutputStream(
                    new FileOutputStream(FILE_NAME)
                )
        ) {

            output.writeObject(scheduleManager);

        } catch (IOException e) {

            System.err.println(
                "데이터 저장 실패: "
                + e.getMessage()
            );
        }
    }


    /**
     * 저장된 일정 데이터를 파일에서 불러온다.
     *
     * 저장 파일이 존재하지 않거나 불러오기에 실패하면
     * 새로운 ScheduleManager를 생성한다.
     *
     * @return 저장된 ScheduleManager 또는 새로운 ScheduleManager
     */
    public static ScheduleManager load() {

        File file = new File(FILE_NAME);

        // 처음 실행해서 저장 파일이 없는 경우
        if (!file.exists()) {
            return new ScheduleManager();
        }


        try (
            ObjectInputStream input =
                new ObjectInputStream(
                    new FileInputStream(file)
                )
        ) {

            return (ScheduleManager) input.readObject();

        } catch (IOException | ClassNotFoundException e) {

            System.err.println(
                "데이터 불러오기 실패: "
                + e.getMessage()
            );

            return new ScheduleManager();
        }
    }
}