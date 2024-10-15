package com.example.NewBackEnd.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse implements Serializable {
    private UUID id;

    private Date createdDate;

    private Date modifiedDate;

    private String createdBy;

    private String modifiedBy;

    private String status;

    public Date getCreatedDate() {
        if (createdDate == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTime(createdDate);
        calendar.add(Calendar.HOUR_OF_DAY, 7);
        return calendar.getTime();
    }
}
