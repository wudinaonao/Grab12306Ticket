package com.naonao.grab12306ticket.version.single.ticket.booking;


import com.naonao.grab12306ticket.version.single.resultclass.ticket.query.QueryTrainInfoReturnResult;
import com.naonao.grab12306ticket.version.single.ticket.login.CheckUserStatus;
import com.naonao.grab12306ticket.version.single.ticket.base.AbstractBooking;
import com.naonao.grab12306ticket.version.single.resultclass.ticket.booking.*;
import lombok.extern.log4j.Log4j;
import org.apache.http.impl.client.CloseableHttpClient;


/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-01 23:53
 **/
@Log4j
public class Booking extends AbstractBooking {

    

    private String secretStr;
    private String trainDate;
    private String backTrainDate;
    private String purposeCode;
    private String queryFromStationName;
    private String queryToStationName;
    private String passengerName;
    private String documentType;
    private String documentNumber;
    private String mobile;
    private String[] seatTypeArr;
    private String expectSeatNumber;


    public Booking(CloseableHttpClient session){
        this.session = session;
    }

    public BookingReturnResult booking(QueryTrainInfoReturnResult queryTrainInfoReturnResult){
        // set variable
        setVariable(queryTrainInfoReturnResult);

        // 1 --> check user login status
        log.info(CHECK_LOGIN_STATUS);
        boolean userStatus = checkUserStatus();
        if (!userStatus){
            log.error(USER_NOT_LOGGED_IN);
            return failedBookingReturnResult(session, USER_NOT_LOGGED_IN);
        }

        // 2 --> submit order request
        log.info(SUBMIT_ORDER_REQUEST);
        SubmitOrderRequestReturnResult submitOrderRequestReturnResult = submitOrderRequest(
                secretStr,
                trainDate,
                backTrainDate,
                purposeCode,
                queryFromStationName,
                queryToStationName
        );
        if (!submitOrderRequestReturnResult.getStatus()){
            log.error(submitOrderRequestReturnResult.getMessage());
            return failedBookingReturnResult(session, submitOrderRequestReturnResult.getMessage());
        }
        session = submitOrderRequestReturnResult.getSession();

        // 3 --> initialization data
        log.info(GET_INITIALIZATION_DATA_AND_SET);
        InitDcReturnResult initDcReturnResult = initDc();
        if (!initDcReturnResult.getStatus()){
            log.error(initDcReturnResult.getMessage());
            return failedBookingReturnResult(session, initDcReturnResult.getMessage());
        }
        session = initDcReturnResult.getSession();

        // 4 --> get passenger DTO
        log.info(GET_PASSENGER_DTO);
        GetPassengerDTOsReturnResult getPassengerDTOsReturnResult = getPassengerDTOs(initDcReturnResult.getRepeatSubmitToken());
        if (!getPassengerDTOsReturnResult.getStatus()){
            log.error(getPassengerDTOsReturnResult.getMessage());
            return failedBookingReturnResult(session, getPassengerDTOsReturnResult.getMessage());
        }
        session = getPassengerDTOsReturnResult.getSession();

        // 5 --> check order information
        log.info(CHECK_ORDER_INFORMATION);
        CheckOrderInfoReturnResult checkOrderInfoReturnResult = checkOrderInfo(
                seatTypeArr,
                passengerName,
                documentType,
                documentNumber,
                mobile,
                initDcReturnResult.getRepeatSubmitToken(),
                initDcReturnResult.getLeftDetails()
        );
        if (!checkOrderInfoReturnResult.getStatus()){
            log.error(checkOrderInfoReturnResult.getMessage());
            return failedBookingReturnResult(session, checkOrderInfoReturnResult.getMessage());
        }
        session = checkOrderInfoReturnResult.getSession();

        // 6 --> get queue count information
        log.info(GET_QUEUE_COUNT_INFORMATION);
        GetQueueCountReturnResult getQueueCountReturnResult = getQueueCount(
                checkOrderInfoReturnResult.getSeatType(),
                initDcReturnResult.getRepeatSubmitToken(),
                initDcReturnResult.getLeftTicketStr(),
                initDcReturnResult.getTrainDateTime(),
                initDcReturnResult.getFromStationTelecode(),
                initDcReturnResult.getPurposeCodes(),
                initDcReturnResult.getStationTrainCode(),
                initDcReturnResult.getToStationTelecode(),
                initDcReturnResult.getTrainLocation(),
                initDcReturnResult.getTrainNo()
        );
        if (!getQueueCountReturnResult.getStatus()){
            log.error(getQueueCountReturnResult.getMessage());
            return failedBookingReturnResult(session, getQueueCountReturnResult.getMessage());
        }
        session = getQueueCountReturnResult.getSession();

        // 7 --> get confirm information
        log.info(GET_CONFIRM_INFORMATION);
        ConfirmSingleForQueueReturnResult confirmSingleForQueueReturnResult = confirmSingleForQueue(
                expectSeatNumber,
                checkOrderInfoReturnResult.getPassengerTicketStr(),
                checkOrderInfoReturnResult.getOldPassengerStr(),
                initDcReturnResult.getKeyCheckIsChange(),
                initDcReturnResult.getLeftTicketStr(),
                initDcReturnResult.getPurposeCodes(),
                initDcReturnResult.getRepeatSubmitToken(),
                initDcReturnResult.getTrainLocation()
        );
        if (!confirmSingleForQueueReturnResult.getStatus()){
            log.error(confirmSingleForQueueReturnResult.getMessage());
            return failedBookingReturnResult(session, confirmSingleForQueueReturnResult.getMessage());
        }
        session = confirmSingleForQueueReturnResult.getSession();

        // 8 --> query order wait time
        log.info(QUERY_ORDER_WAIT_TIME);
        QueryOrderWaitTimeReturnResult queryOrderWaitTimeReturnResult = queryOrderWaitTime(initDcReturnResult.getRepeatSubmitToken());
        if (!queryOrderWaitTimeReturnResult.getStatus()){
            log.info(QUERY_ORDER_WAIT_TIME_FAILED);
            return successBookingReturnResult(session, BOOKING_MAY_HAVE_BEEN_SUCCESSFUL);

        }
        session = queryOrderWaitTimeReturnResult.getSession();

        // 9 --> try get order result from queue
        log.info(TRY_GET_ORDER_RESULT_FROM_QUEUE);
        ResultOrderForDcQueueReturnResult resultOrderForDcQueueReturnResult = resultOrderForDcQueue(
                queryOrderWaitTimeReturnResult.getOrderId(),
                initDcReturnResult.getRepeatSubmitToken()
        );
        if (!resultOrderForDcQueueReturnResult.getStatus()){
            log.error(resultOrderForDcQueueReturnResult.getMessage());
            return failedBookingReturnResult(session, resultOrderForDcQueueReturnResult.getMessage());
        }
        session = resultOrderForDcQueueReturnResult.getSession();

        // 10 --> get booking result
        log.info(GET_BOOKING_RESULT);
        ResultBookingTicketHtmlReturnResult resultBookingTicketHtmlReturnResult = resultBookingTicketHtml(initDcReturnResult.getRepeatSubmitToken());
        if (!resultBookingTicketHtmlReturnResult.getStatus()){
            log.error(resultBookingTicketHtmlReturnResult.getMessage());
            return failedBookingReturnResult(session, resultBookingTicketHtmlReturnResult.getMessage());
        }
        session = resultBookingTicketHtmlReturnResult.getSession();

        // success
        return successBookingReturnResult(
                session,
                BOOKING_SUCCESS,
                // result string
                bookingTicketResultToString(resultBookingTicketHtmlReturnResult),
                // result object
                resultBookingTicketHtmlReturnResult
        );

    }

    private void setVariable(QueryTrainInfoReturnResult queryTrainInfoReturnResult){
        secretStr = queryTrainInfoReturnResult.getTrainInfoList().get(0).getSecretStr();
        trainDate = formatDate(queryTrainInfoReturnResult.getTrainInfoList().get(0).getTrainDate());
        // backTrainDate may is trainDate, identical
        backTrainDate = queryTrainInfoReturnResult.getGrabTicketInformationEntity().getBackTrainDate();
        purposeCode = queryTrainInfoReturnResult.getGrabTicketInformationEntity().getPurposeCode();
        queryFromStationName = queryTrainInfoReturnResult.getGrabTicketInformationEntity().getFromStation();
        queryToStationName = queryTrainInfoReturnResult.getGrabTicketInformationEntity().getToStation();
        passengerName = queryTrainInfoReturnResult.getGrabTicketInformationEntity().getPassengerName();
        documentType = queryTrainInfoReturnResult.getGrabTicketInformationEntity().getDocumentType();
        documentNumber = queryTrainInfoReturnResult.getGrabTicketInformationEntity().getDocumentNumber();
        mobile = queryTrainInfoReturnResult.getGrabTicketInformationEntity().getMobile();
        seatTypeArr = queryTrainInfoReturnResult.getGrabTicketInformationEntity().getSeatType().split(",");
        expectSeatNumber = queryTrainInfoReturnResult.getGrabTicketInformationEntity().getExpectSeatNumber();
    }

    private Boolean checkUserStatus(){
        CheckUserStatus checkUserStatus = new CheckUserStatus(session);
        return checkUserStatus.checkUserStatus();
    }

    private SubmitOrderRequestReturnResult submitOrderRequest(String secretStr,
                                                              String trainDate,
                                                              String backTrainDate,
                                                              String purposeCode,
                                                              String queryFromStationName,
                                                              String queryToStationName){
        SubmitOrderRequest submitOrderRequest = new SubmitOrderRequest(session);
        return submitOrderRequest.submitOrderRequest(
                secretStr,
                trainDate,
                backTrainDate,
                purposeCode,
                queryFromStationName,
                queryToStationName
        );
    }

    private InitDcReturnResult initDc(){
        InitDc initDc = new InitDc(session);
        return initDc.initDc();
    }

    private GetPassengerDTOsReturnResult getPassengerDTOs(String repeatSubmitToken){
        GetPassengerDTOs getPassengerDTOs = new GetPassengerDTOs(session);
        return getPassengerDTOs.getPassengerDto(repeatSubmitToken);
    }

    private CheckOrderInfoReturnResult checkOrderInfo(String[] seatTypeArr,
                                                      String passengerName,
                                                      String documentType,
                                                      String documentNumber,
                                                      String mobile,
                                                      String repeatSubmitToken,
                                                      String[] leftDetails){
        CheckOrderInfo checkOrderInfo = new CheckOrderInfo(session);
        return checkOrderInfo.checkOrderInfo(
                seatTypeArr,
                passengerName,
                documentType,
                documentNumber,
                mobile,
                repeatSubmitToken,
                leftDetails
        );
    }

    private GetQueueCountReturnResult getQueueCount(String seatType,
                                                    String repeatSubmitToken,
                                                    String leftTicketStr,
                                                    String trainDateTime,
                                                    String fromStationTelecode,
                                                    String purposeCodes,
                                                    String stationTrainCode,
                                                    String toStationTelecode,
                                                    String trainLocation,
                                                    String trainNo){
        GetQueueCount getQueueCount = new GetQueueCount(session);
        return getQueueCount.getQueueCount(
                seatType,
                repeatSubmitToken,
                leftTicketStr,
                trainDateTime,
                fromStationTelecode,
                purposeCodes,
                stationTrainCode,
                toStationTelecode,
                trainLocation,trainNo
        );
    }

    private ConfirmSingleForQueueReturnResult confirmSingleForQueue(String expectSeatNumber,
                                                                    String passengerTicketStr,
                                                                    String oldPassengerStr,
                                                                    String keyCheckIsChange,
                                                                    String leftTicketStr,
                                                                    String purposeCodes,
                                                                    String repeatSubmitToken,
                                                                    String trainLocation){
        ConfirmSingleForQueue confirmSingleForQueue = new ConfirmSingleForQueue(session);
        return confirmSingleForQueue.confirmSingleForQueue(
                expectSeatNumber,
                passengerTicketStr,
                oldPassengerStr,
                keyCheckIsChange,
                leftTicketStr,
                purposeCodes,
                repeatSubmitToken,
                trainLocation
        );
    }

    private QueryOrderWaitTimeReturnResult queryOrderWaitTime(String repeatSubmitToken){
        QueryOrderWaitTime queryOrderWaitTime = new QueryOrderWaitTime(session);
        return queryOrderWaitTime.queryOrderWaitTime(repeatSubmitToken);
    }

    private ResultOrderForDcQueueReturnResult resultOrderForDcQueue(String orderId,
                                                                    String repeatSubmitToken){
        ResultOrderForDcQueue resultOrderForDcQueue = new ResultOrderForDcQueue(session);
        return resultOrderForDcQueue.resultOrderForQueue(orderId, repeatSubmitToken);
    }

    private ResultBookingTicketHtmlReturnResult resultBookingTicketHtml(String repeatSubmitToken){
        ResultBookingTicketHtml resultBookingTicketHtml = new ResultBookingTicketHtml(session);
        return resultBookingTicketHtml.resultBookingTicketHtml(repeatSubmitToken);
    }

    /**
     * get booking result string
     *
     * @return      booking result
     */
    private String bookingTicketResultToString(ResultBookingTicketHtmlReturnResult resultBookingTicketHtmlReturnResult){
        // BookingTicketResultInfo bookingTicketResultInfo = new BookingTicketResultInfo(bookingTicketResultStr);
        StringBuilder resultStr = new StringBuilder();
        String[] content = {"席位已锁定，请于30分钟内支付，超时将取消订单！\n",
                "订单号码：" + resultBookingTicketHtmlReturnResult.getSequenceNo() + "\n",
                "证件类型：" + resultBookingTicketHtmlReturnResult.getPassengerIdTypeName() + "\n",
                "证件号码：" + resultBookingTicketHtmlReturnResult.getPassengerIdNo() + "\n",
                "乘客姓名：" + resultBookingTicketHtmlReturnResult.getPassengerName() + "\n",
                "车厢号码：" + resultBookingTicketHtmlReturnResult.getCoachName() + "\n",
                "座位号码：" + resultBookingTicketHtmlReturnResult.getSeatName() + "\n",
                "座位类型：" + resultBookingTicketHtmlReturnResult.getSeatTypeName() + "\n",
                "出发站点：" + resultBookingTicketHtmlReturnResult.getFromStationName() + "\n",
                "到达站点：" + resultBookingTicketHtmlReturnResult.getToStationName() + "\n",
                "列车车次：" + resultBookingTicketHtmlReturnResult.getStationTrainCode() + "\n",
                "出发日期：" + resultBookingTicketHtmlReturnResult.getStartTrainDate() + "\n",
                "车票价格：" + resultBookingTicketHtmlReturnResult.getTicketPrice() + "\n",
                "车票号码：" + resultBookingTicketHtmlReturnResult.getTicketNo() + "\n",
                "车票类型：" + resultBookingTicketHtmlReturnResult.getTicketTypeName()};
        for(String element: content){
            resultStr.append(element);
        }
        return resultStr.toString();
    }

    private String formatDate(String date){
        String year = date.substring(0, 4);
        String month = date.substring(4, 6);
        String day = date.substring(6, 8);
        return year + "-" + month + "-" + day;
    }

    private BookingReturnResult failedBookingReturnResult(CloseableHttpClient session, String message){
        BookingReturnResult bookingReturnResult = new BookingReturnResult();
        bookingReturnResult.setStatus(false);
        bookingReturnResult.setSession(session);
        bookingReturnResult.setMessage(message);
        return bookingReturnResult;
    }

    private BookingReturnResult successBookingReturnResult(CloseableHttpClient session, String message){
        BookingReturnResult bookingReturnResult = new BookingReturnResult();
        bookingReturnResult.setStatus(true);
        bookingReturnResult.setSession(session);
        bookingReturnResult.setMessage(message);
        return bookingReturnResult;
    }

    private BookingReturnResult successBookingReturnResult(CloseableHttpClient session,
                                                           String message,
                                                           String bookingResultString,
                                                           ResultBookingTicketHtmlReturnResult resultBookingTicketHtmlReturnResult){
        BookingReturnResult bookingReturnResult = new BookingReturnResult();
        bookingReturnResult.setStatus(true);
        bookingReturnResult.setSession(session);
        bookingReturnResult.setMessage(message);
        bookingReturnResult.setBookingResultString(bookingResultString);
        bookingReturnResult.setBookingResultObject(resultBookingTicketHtmlReturnResult);
        return bookingReturnResult;
    }

}
