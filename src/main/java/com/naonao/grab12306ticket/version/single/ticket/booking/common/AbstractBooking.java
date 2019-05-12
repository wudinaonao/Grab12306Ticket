package com.naonao.grab12306ticket.version.single.ticket.booking.common;

import com.naonao.grab12306ticket.version.single.ticket.common.AbstractTicket;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * @program: 12306grabticket_java
 * @description: abstract booking
 * @author: Wen lyuzhao
 * @create: 2019-05-01 23:31
 **/
public abstract class AbstractBooking extends AbstractTicket {

    // protected static final Integer HTTP_SUCCESS = 200;


    protected static final String STATUS = "status";

    protected static final String SUBMIT_ORDER_REQUEST_SUCCESS = "submit order request success";
    protected static final String SUBMIT_ORDER_REQUEST_FAILED = "submit order request failed";

    protected static final String INIT_DC_SUCCESS = "initialization dc success";
    protected static final String INIT_DC_FAILED  = "initialization dc failed";

    protected static final String GET_PASSENGER_DTO_SUCCESS = "get passenger DTO success";
    protected static final String GET_PASSENGER_DTO_FAILED = "get passenger DTO failed";

    protected static final String NO_SUITABLE_SEAT_TYPE = "no suitable seat type";
    protected static final String NO_SEAT_TYPE_OBTAINED = "No seat type obtained";
    protected static final String CHECK_ORDER_INFO_FAILED = "check order information failed";
    protected static final String CHECK_ORDER_INFO_SUCCESS = "check order information success";

    protected static final String GET_QUEUE_COUNT_FAILED = "get queue count failed";
    protected static final String GET_QUEUE_COUNT_SUCCESS = "get queue count success";

    protected static final String REPEAT_SUBMIT_TOKEN_OR_LEFT_TICKET_STR_EMPTY = "repeatSubmitToken or leftTicketStr is empty";

    protected static final String CONFIREM_SINGLE_FOR_QUEUE_FAILED = "confirm single for queue failed";
    protected static final String CONFIREM_SINGLE_FOR_QUEUE_SUCCESS = "confirm single for queue success";

    protected static final String QUERY_ORDER_WAIT_TIME_SUCCESS = "query order wait time success";

    protected static final String RESULT_ORDER_FOR_QUEUE_FAILED = "get result order for queue failed";
    protected static final String RESULT_ORDER_FOR_QUEUE_SUCCESS= "get result order for queue success";

    protected static final String RESULT_BOOKING_TICKET_HTML_FAILED = "try get booing ticket result from html failed";
    protected static final String RESULT_BOOKING_TICKET_HTML_SUCCESS = "try get booing ticket result from html success";

    protected static final String USER_NOT_LOGGED_IN = "user not logged in";

    // booking class
    protected static final String CHECK_LOGIN_STATUS = "check login status";
    protected static final String SUBMIT_ORDER_REQUEST = "submit order request";
    protected static final String GET_INITIALIZATION_DATA_AND_SET = "get initialization data and set";
    protected static final String GET_PASSENGER_DTO = "get passenger DTO";
    protected static final String CHECK_ORDER_INFORMATION = "check order information";
    protected static final String GET_QUEUE_COUNT_INFORMATION = "get queue count information";
    protected static final String GET_CONFIRM_INFORMATION = "get confirm information";
    protected static final String QUERY_ORDER_WAIT_TIME = "query order wait time";
    protected static final String QUERY_ORDER_WAIT_TIME_FAILED = "query order wait time failed, but may have successfully booked a ticket.";
    protected static final String BOOKING_MAY_HAVE_BEEN_SUCCESSFUL = "the booking may have been successful, please visit the 12306 for details.";
    protected static final String TRY_GET_ORDER_RESULT_FROM_QUEUE = "try get order result from queue";
    protected static final String GET_BOOKING_RESULT = "get booking result";
    protected static final String BOOKING_SUCCESS = "booking success";


    protected CloseableHttpClient session;

}
