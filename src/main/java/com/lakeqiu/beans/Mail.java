package com.lakeqiu.beans;

import lombok.*;

import java.util.Set;

/**
 * @author lakeqiu
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Mail {

    /**
     * 主题
     * 内存
     * 收件人
     */
    private String subject;
    private String message;
    private Set<String> receivers;
}
