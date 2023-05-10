package com.baker.resumeanalysis;

import com.baker.pojo.ResumeInformation;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SpringBootTest
class ResumeAnalysisApplicationTests {


    @Resource
    private MongoTemplate mongoTemplate;
    @Test
    void contextLoads() {
        Query query = new Query();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2023, Calendar.MAY,8);
        var t1 = calendar.getTime();
        calendar.set(2023,Calendar.MAY,9);
        var t2 = calendar.getTime();
        System.out.println(t1);
        System.out.println(t2);
        Criteria c = Criteria.where("createDate").gte(t1).lte(t2);
        query.addCriteria(c);
        var s = mongoTemplate.find(query, ResumeInformation.class);
        s.forEach(System.out::println);
    }

    @Test
    void t1() throws ParseException {
        String date = "2023-5-8";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        var t = format.parse(date);
        Calendar c = Calendar.getInstance();
        c.setTime(t);
        c.set(Calendar.DAY_OF_MONTH,c.get(Calendar.DAY_OF_MONTH)+1);

    }

}
