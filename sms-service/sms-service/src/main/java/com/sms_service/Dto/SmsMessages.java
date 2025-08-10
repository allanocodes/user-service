package com.sms_service.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SmsMessages {

private String from;
private List<Destinations> destinations;
private String text;

}
