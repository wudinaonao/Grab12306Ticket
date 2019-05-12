package com.naonao.grab12306ticket.version.single.test;

import com.naonao.grab12306ticket.version.single.constants.ConvertMap;
import com.naonao.grab12306ticket.version.single.tools.ComputeHash;
import com.naonao.grab12306ticket.version.single.tools.GeneralTools;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: 12306grabticket_java
 * @description:
 * @author: Wen lyuzhao
 * @create: 2019-05-10 03:19
 **/
@Log4j
public class CreateInformation {

    private static final String NUMBER_CHARSET = "0123456789";
    private static final String LOWER_SUBTITLE_CHARSET = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPER_SUBTITLE_CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALL_SUBTITLE_CHARSET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String ALL_CHARSET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String CHINESE_CHARSET = "赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜戚谢邹喻柏水窦章云苏潘葛奚范彭郎鲁韦昌马苗凤花方俞任袁柳酆鲍史唐费廉岑薛雷贺倪汤滕殷罗毕郝邬安常乐于时傅皮卞齐康伍余元卜顾孟平黄和穆萧尹姚邵湛汪祁毛禹狄米贝明臧计伏成戴谈宋茅庞熊纪舒屈项祝董梁杜阮蓝闵席季麻强贾路娄危江童颜郭梅盛林刁锺徐邱骆高夏蔡田樊胡凌霍虞万支柯昝管卢莫经房裘缪干解应宗丁宣贲邓郁单杭洪包诸左石崔吉钮龚程嵇邢滑裴陆荣翁荀羊於惠甄麹家封芮羿储靳汲邴糜松井段富巫乌焦巴弓牧隗山谷车侯宓蓬全郗班仰秋仲伊宫甯仇栾暴甘钭厉戎祖武符刘景詹束龙叶幸司韶郜黎蓟薄印宿白怀蒲邰从鄂索咸籍赖卓蔺屠蒙池乔阴鬱胥能苍双闻莘党翟谭贡劳逄姬申扶堵冉宰郦雍郤璩桑桂濮牛寿通边扈燕冀郏浦尚农温别庄晏柴瞿阎充慕连茹习宦艾鱼容向古易慎戈廖庾终暨居衡步都耿满弘匡国文寇广禄阙东欧殳沃利蔚越夔隆师巩厍聂晁勾敖融冷訾辛阚那简饶空曾毋沙乜养鞠须丰巢关蒯相查后荆红游竺权逯盖益桓公万俟司马上官欧阳夏侯诸葛闻人东方赫连皇甫尉迟公羊澹台公冶宗政濮阳淳于单于太叔申屠公孙仲孙轩辕令狐锺离宇文长孙慕容鲜于闾丘司徒司空亓官司寇仉督子车颛孙端木巫马公西漆雕乐正壤驷公良拓跋夹谷宰父穀梁晋楚闫法汝鄢涂钦段干百里东郭南门呼延归海羊舌微生岳帅缑亢况後有琴梁丘左丘东门西门商牟佘佴伯赏南宫墨哈谯笪年爱阳佟第五言福";

    protected List<String> cityNameList;
    protected List<String> seatType;
    
    protected CreateInformation(){
        this.cityNameList = ConvertMap.cityNameList();
        // create seatType list
        List<String> seatType = new ArrayList<>();
        seatType.add("商务座");
        seatType.add("一等座");
        seatType.add("二等座");
        seatType.add("无座");
        seatType.add("硬座");
        seatType.add("硬卧");
        seatType.add("软卧");
        seatType.add("高级软卧");
        this.seatType = seatType;
    }

    protected String intToString(Integer value){
        return String.valueOf(value);
    }
    protected String intToString(Integer value, String format){
        return String.format(format, value);
    }

    
    protected String username12306(){
        return RandomStringUtils.random(10, ALL_SUBTITLE_CHARSET);
    }
    protected String password12306(){
        return RandomStringUtils.random(12, ALL_SUBTITLE_CHARSET);
    }
    
    protected String afterTime(){
        String h = intToString(RandomUtils.nextInt(0, 24), "%02d");
        String min = intToString(RandomUtils.nextInt(0, 60),"%02d");
        return h + ":" + min;
    }
    protected String beforeTime(){
        String h = intToString(RandomUtils.nextInt(0, 24), "%02d");
        String min = intToString(RandomUtils.nextInt(0, 60), "%02d");
        return h + ":" + min;
    }
    protected String tranDate(){
        String year = intToString(RandomUtils.nextInt(2018, 2020));
        String month = intToString(RandomUtils.nextInt(1, 13), "%02d");
        String day = intToString(RandomUtils.nextInt(1, 25), "%02d");
        return year + "-" + month + "-" + day;
    }
    protected String fromStation(){
        return cityNameList.get(RandomUtils.nextInt(0, cityNameList.size()));
    }
    protected String toStation(){
        return cityNameList.get(RandomUtils.nextInt(0, cityNameList.size()));
    }
    protected String purposeCode(){
        return "ADULT";
    }
    protected String trainName(){
        // String subtitle = RandomStringUtils.random(1, UPPER_SUBTITLE_CHARSET);
        // String number = RandomStringUtils.random(4, NUMBER_CHARSET);
        // return subtitle + number;
        return "";
    }
    protected String backTrainDate(){
        return tranDate();
    }
    protected String passengerName(){
        return RandomStringUtils.random(3, CHINESE_CHARSET);
    }
    protected String documentType(){
        return "1";
    }
    protected String documentNumber(){
        return RandomStringUtils.random(18, NUMBER_CHARSET);
    }
    protected String mobile(){
        return "86" + RandomStringUtils.random(11, NUMBER_CHARSET);
    }
    protected String seatType(){
        return seatType.get(RandomUtils.nextInt(0, seatType.size()));
    }
    protected String expectSeatNumber(){
        return "A";
    }
    
    protected String receiverEmail(){
        return RandomStringUtils.random(8, ALL_SUBTITLE_CHARSET) + "@gmail.com";
    }
    protected String sendEmail(){
        return RandomStringUtils.random(8, ALL_SUBTITLE_CHARSET) + "@gmail.com";
    }
    protected String sendEmailHost(){
        return "stmp.gmail.com";
    }
    protected String sendEmailPort(){
        return RandomStringUtils.random(4, NUMBER_CHARSET);
    }
    protected String sendEmailUsername(){
        return RandomStringUtils.random(10, ALL_SUBTITLE_CHARSET);
    }
    protected String sendEmailPassword(){
        return RandomStringUtils.random(10, ALL_SUBTITLE_CHARSET);
    }
    protected String receiverPhone(){
        return "86" + RandomStringUtils.random(11, NUMBER_CHARSET);
    }
    protected String notifyMode(){
        return "email";
    }
    
    protected String status(){
        return "wait";
    }
    protected String taskStartTime(){
        return GeneralTools.formatTime();
    }
    protected String taskEndTime(){
        return GeneralTools.formatTime();
    }
    protected String taskLastRunTime(){
        return GeneralTools.formatTime();
    }
    protected Long tryCount(){
        return RandomUtils.nextLong(0, 1000);
    }
    protected String message(){
        return "wait";
    }
    protected String hash(){
        return ComputeHash.md5(RandomStringUtils.random(10, ALL_CHARSET));
    }


}
