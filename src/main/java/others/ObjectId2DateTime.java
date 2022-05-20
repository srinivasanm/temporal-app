package others;

import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ObjectId2DateTime {

    public static void main(String args[]) {

        System.out.println("Min Object Id ");
        String objectIdMin = "62751dbbf974d130d938f407";
        long startTime = printObjectIdTimestamp(objectIdMin);

        System.out.println("Max Object Id ");
        String objectId = "62751e00f974d130d93d804b";
        long endTime = printObjectIdTimestamp(objectId);

        divideTimeRangeIntoIntervals(startTime,endTime,6);
    }

    private static long printObjectIdTimestamp(String objectId) {
        long objectIdTimestamp = Long.parseLong(objectId.substring(0, 8), 16) * 1000;
        System.out.println("Long value "+objectIdTimestamp);
        Date d = new Date(objectIdTimestamp);
        System.out.println("Date "+d);
        System.out.println("Instant "+d.toInstant());
        return objectIdTimestamp;
    }

    private static void divideTimeRangeIntoIntervals(long startTime,long endTime,int intervals){
        long tickSpan = endTime-startTime;
        long tickInterval = tickSpan / intervals;
        System.out.println("startTime "+startTime);
        System.out.println("tickSpan : "+tickSpan);
        System.out.println("tickInterval : "+tickInterval);
        List<Date> dateTimeInRange = new ArrayList<>();
        for(long i=startTime;i<endTime;){
            dateTimeInRange.add(new Date(i));
            i =i+tickInterval;
        }

        System.out.println("Date Ranges :");
        dateTimeInRange.stream().forEach((d)->System.out.println("date : "+d+" object id "+new ObjectId(d)));
    }
}
