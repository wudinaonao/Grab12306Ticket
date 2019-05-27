package com.naonao.grab12306ticket.version.single.scheduler.method;

import com.naonao.grab12306ticket.version.single.constants.TaskStatusName;
import com.naonao.grab12306ticket.version.single.entity.GrabTicketInformationEntity;
import com.naonao.grab12306ticket.version.single.entity.NotificationInformationEntity;
import com.naonao.grab12306ticket.version.single.notification.Notification;
import com.naonao.grab12306ticket.version.single.resultclass.ticket.booking.BookingReturnResult;
import com.naonao.grab12306ticket.version.single.resultclass.ticket.login.LoginReturnResult;
import com.naonao.grab12306ticket.version.single.resultclass.ticket.query.QueryTrainInfoReturnResult;
import com.naonao.grab12306ticket.version.single.scheduler.base.AbstractScheduler;
import com.naonao.grab12306ticket.version.single.ticket.booking.Booking;
import com.naonao.grab12306ticket.version.single.ticket.login.Login;
import com.naonao.grab12306ticket.version.single.tools.ComputeHash;
import com.naonao.grab12306ticket.version.single.tools.HttpTools;
import lombok.extern.log4j.Log4j;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.Set;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-08 22:13
 **/
@Log4j
public class QueryTrainInfoReturnResultHandle extends AbstractScheduler {

    /**
     * here is QueryTrainInfoReturnResult handle method
     */
    private BookingReturnResult bookingReturnResult;
    private Set<String> hashSet;
    private QueryTrainInfoReturnResult queryTrainInfoReturnResult;

    /**
     * single version
     * @param queryTrainInfoReturnResult    queryTrainInfoReturnResult
     * @param hashSet                       global hash set use to save task hash, make only one
     *                                      of the same tasks in progress, avoid unpredictable errors
     *                                      caused by concurrent bookings.
     */
    public QueryTrainInfoReturnResultHandle(QueryTrainInfoReturnResult queryTrainInfoReturnResult,
                                            Set<String> hashSet){
        this.queryTrainInfoReturnResult = queryTrainInfoReturnResult;
        this.hashSet = hashSet;
    }

    /**
     * single version
     *
     * QueryTrainInfoReturnResult handle method
     * first get a QueryTrainInfoReturnResult object
     *
     *  1. check task is booking, hash exist in set.
     *  2. check queryTrainInfoReturnResult is true, else delete hash in set.
     *  3. check bookingReturnResult is true, else delete hash in set.
     *  4. check send notification is true, else print log and exit programme.
     *  5. booking succeed, print log and exit programme.
     *
     */
    public void handle(){
        // not found eligible train information
        if (!isTure(queryTrainInfoReturnResult)){
            deleteTaskBySet(queryTrainInfoReturnResult);
            log.error("queryTrainInfoReturnResult is false.");
            return;
        }
        // booking failed
        // if (!booking(queryTrainInfoReturnResult)){
        //     deleteTaskBySet(queryTrainInfoReturnResult);
        //     log.error(TaskStatusName.BOOKING_FAILED.getTaskStatusName());
        //     return;
        // }
        // send notification failed
        if (!sendNotification(bookingReturnResult, queryTrainInfoReturnResult.getNotificationInformationEntity())){
            log.info(TaskStatusName.BOOKING_SUCCEED_BUT_SEND_NOTIFICATION_FAILED.getTaskStatusName());
            System.exit(0);
        }
        // success and exit programme
        log.info(TaskStatusName.COMPLETED.getTaskStatusName());
        System.exit(0);
    }

    /**
     * check queryTrainInfoReturnResult is true.
     * if true, then have train information.
     *
     * @param queryTrainInfoReturnResult    queryTrainInfoReturnResult
     * @return                              Boolean
     */
    private Boolean isTure(QueryTrainInfoReturnResult queryTrainInfoReturnResult){
        return queryTrainInfoReturnResult.getStatus();
    }

    /**
     * booking ticket
     *
     * @param queryTrainInfoReturnResult    queryTrainInfoReturnResult
     * @return                              Boolean
     */
    private Boolean booking(QueryTrainInfoReturnResult queryTrainInfoReturnResult){

        // produce

        String username12306 = queryTrainInfoReturnResult.getUserInformationEntity().getUsername12306();
        String password12306 = queryTrainInfoReturnResult.getUserInformationEntity().getPassword12306();
        // Login
        CloseableHttpClient session = HttpTools.getSession(30000);
        Login login = new Login(session);
        LoginReturnResult loginReturnResult = login.login(username12306, password12306);
        if (!loginReturnResult.getStatus()){
            log.error(loginReturnResult.getMessage());
            return false;
        }
        session = loginReturnResult.getSession();
        // Booking
        Booking booking = new Booking(session);
        BookingReturnResult bookingReturnResult = booking.booking(queryTrainInfoReturnResult);
        if (!bookingReturnResult.getStatus()){
            log.error(bookingReturnResult.getMessage());
            return false;
        }
        this.bookingReturnResult = bookingReturnResult;
        return true;

        //test
        // int a = RandomUtils.nextInt(0, 100);
        // if (a % 2 == 0){
        //     return true;
        // }
        // return false;
    }

    /**
     * send notification
     * @param bookingReturnResult       bookingReturnResult
     * @return                          Boolean
     */
    private Boolean sendNotification(BookingReturnResult bookingReturnResult, NotificationInformationEntity notificationInformationEntity){

        // produce

        // here is send notification method
        // if (bookingReturnResult == null){
        //     return false;
        // }
        return new Notification().sendNotification(bookingReturnResult, notificationInformationEntity);

        // // test
        // int a = RandomUtils.nextInt(0, 100);
        // if (a % 2 == 0){
        //     return true;
        // }
        // return false;

    }

    private void deleteTaskBySet(QueryTrainInfoReturnResult queryTrainInfoReturnResult){
        GrabTicketInformationEntity grabTicketInformationEntity = queryTrainInfoReturnResult.getGrabTicketInformationEntity();
        hashSet.remove(ComputeHash.fromGrabTicketInformation(grabTicketInformationEntity));
    }

}
