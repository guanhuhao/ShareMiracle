package com.sharemiracle.utils;

import com.sharemiracle.dto.UserDTO;

/**
 * 通过ThreadLocal存取当前线程用户
 */
public class UserHolder {
    private static final ThreadLocal<UserDTO> tl = new ThreadLocal<>();

    public static void saveUser(UserDTO user){
        tl.set(user);
    }

    public static UserDTO getUser(){
        return tl.get();
    }

    public static void removeUser(){
        tl.remove();
    }
}
