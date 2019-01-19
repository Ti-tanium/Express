package com.example.asus.manager.model;

public class Postman  {
    /** 邮递员ID */
    private String id ;
    /** 名字;id唯一标识 */
    private String name ;
    /** 密码 */
    private String password ;

    /** 邮递员ID */
    public String getId(){
        return this.id;
    }
    /** 邮递员ID */
    public void setId(String id){
        this.id = id;
    }
    /** 名字;id唯一标识 */
    public String getName(){
        return this.name;
    }
    /** 名字;id唯一标识 */
    public void setName(String name){
        this.name = name;
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