package com.example.asus.manager.model;

import java.io.Serializable;



public class Pack {
    /** 邮件ID */
    private Integer id ;
    /** 目的地址 */
    private String destination ;
    /** 源地址 */
    private String source ;
    /** 寄件人姓名 */
    private String mailer ;
    /** 寄件人电话 */
    private String mailerPhone ;
    /** 收件人姓名 */
    private String receiver ;
    /** 收件人电话 */
    private String receiverPhone ;
    /** 密码 */
    private String password ;

    /** 邮件ID */
    public Integer getId(){
        return this.id;
    }
    /** 邮件ID */
    public void setId(Integer id){
        this.id = id;
    }
    /** 目的地址 */
    public String getDestination(){
        return this.destination;
    }
    /** 目的地址 */
    public void setDestination(String destination){
        this.destination = destination;
    }
    /** 源地址 */
    public String getSource(){
        return this.source;
    }
    /** 源地址 */
    public void setSource(String source){
        this.source = source;
    }
    /** 寄件人姓名 */
    public String getMailer(){
        return this.mailer;
    }
    /** 寄件人姓名 */
    public void setMailer(String mailer){
        this.mailer = mailer;
    }
    /** 寄件人电话 */
    public String getMailerPhone(){
        return this.mailerPhone;
    }
    /** 寄件人电话 */
    public void setMailerPhone(String mailerPhone){
        this.mailerPhone = mailerPhone;
    }
    /** 收件人姓名 */
    public String getReceiver(){
        return this.receiver;
    }
    /** 收件人姓名 */
    public void setReceiver(String receiver){
        this.receiver = receiver;
    }
    /** 收件人电话 */
    public String getReceiverPhone(){
        return this.receiverPhone;
    }
    /** 收件人电话 */
    public void setReceiverPhone(String receiverPhone){
        this.receiverPhone = receiverPhone;
    }
    /** 密码 */
    public String getPassword(){
        return this.password;
    }
    /** 密码 */
    public void setPassword(String password){
        this.password = password;
    }
}
