package com.example.demo.method;
import android.util.Log;

import com.example.demo.models.User;
import com.example.demo.models.Users;
import com.example.demo.models.name_head_url;

import java.security.MessageDigest;
import java.util.List;
import java.util.Random;

public class IMreg {


    /**
     * @return 15位随机数
     */
    public static String getrandom(){
        Random ran=new Random();
        int a=ran.nextInt(99999999);
        int b=ran.nextInt(99999999);
        long l=a*10000000L+b;
        String num=String.valueOf(l);
        return num;
    }

    //SHA1算法
    public static String sha1(final String string) {
        try {
            if (string == null) {
                return null;
            }

            final MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            final byte[] digests = messageDigest.digest(string.getBytes());

            final StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < digests.length; i++) {
                int halfbyte = (digests[i] >>> 4) & 0x0F;
                for (int j = 0; j <= 1; j++) {
                    stringBuilder.append(
                            ((0 <= halfbyte) && (halfbyte <= 9))
                                    ? (char) ('0' + halfbyte)
                                    : (char) ('a' + (halfbyte - 10)));
                    halfbyte = digests[i] & 0x0F;
                }
            }

            return stringBuilder.toString();
        } catch (final Throwable throwable) {
            Log.d("error converting", throwable.toString());
            return null;
        }
    }


    //根据id找name和head_url

    public static name_head_url getname_head_url(Users users,int id){

        List<User> userslist = users.getUsers();

        for(int i=0;i<userslist.size();i++){

            User user = userslist.get(i);
            if(user.getId()==id){

                return new name_head_url(user.getUsername(),user.getHead_url());


            }
        }
        return null;

    }


}
