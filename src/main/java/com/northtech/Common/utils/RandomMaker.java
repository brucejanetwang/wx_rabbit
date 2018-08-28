package com.northtech.Common.utils;

import java.util.Random;

public class RandomMaker {
    public final int TYPE_NUMBER = 0;
    public final int TYPE_WORD = 1;
    private String createNumber(int number){
        char[] codeSeq = {'0','1','2', '3', '4', '5', '6', '7', '8', '9' };
        Random random = new Random();
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < number; i++) {
            String r = String.valueOf(codeSeq[random.nextInt(codeSeq.length)]);
            s.append(r);
        }
        return s.toString();
    }
    private String createWord(int number){
        char[] codeSeq = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J',
                'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9' };
        Random random = new Random();
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < number; i++) {
            String r = String.valueOf(codeSeq[random.nextInt(codeSeq.length)]);
            s.append(r);
        }
        return s.toString();
    }

    public String create(int type,int number) {
        if (type == TYPE_NUMBER){
            return createNumber(number);
        }else if (type == TYPE_WORD){
            return createWord(number);
        }
        return null;
    }

    public static void main(String[] agr){
        System.out.println(  (new RandomMaker()).create(0,5));
        System.out.println(  (new RandomMaker()).create(1,5));
        System.out.println(  (new RandomMaker()).create(2,5));
    }
}
