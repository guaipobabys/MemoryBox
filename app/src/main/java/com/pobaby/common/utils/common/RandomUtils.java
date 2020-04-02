package com.pobaby.common.utils.common;

import com.pobaby.memorybox.ui.mvp.model.other.ConfigurationModel;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 随机工具类
 *
 * @author chenqh
 * @email 403167386@qq.com
 * created at 2019/10/22 9:44
 */
public class RandomUtils {

    private static List<String> mUppercaseList = Arrays.asList(
            "A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z");

    private static List<String> mLowercaseList = Arrays.asList(
            "a", "b", "c", "d", "e", "f", "g",
            "h", "i", "j", "k", "l", "m", "n",
            "o", "p", "q", "r", "s", "t",
            "u", "v", "w", "x", "y", "z");

    private static List<String> mNumberList = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");

    private static List<String> mSpecialList = Arrays.asList("!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "+", "=");

    public static String genRandomString(int length) {

        List<String> list = new LinkedList<>();
        List<ConfigurationModel> models = ConfigurationModel.loadAll();
        if (models != null && models.size() > 0) {
            ConfigurationModel model = models.get(0);
            EnumValues.PwdRuleType pwdRuleType = EnumValues.PwdRuleType.values()[model.getPwdRuleType()];
            switch (pwdRuleType) {
                case 大小写字母:
                    list.addAll(mUppercaseList);
                    list.addAll(mLowercaseList);
                    break;
                case 数字:
                    list.addAll(mNumberList);
                    break;
                case 特殊字符:
                    list.addAll(mSpecialList);
                    break;
                case 大小写字母_数字:
                    list.addAll(mUppercaseList);
                    list.addAll(mLowercaseList);
                    list.addAll(mNumberList);
                    break;
                case 大小写字母_特殊字符:
                    list.addAll(mUppercaseList);
                    list.addAll(mLowercaseList);
                    list.addAll(mSpecialList);
                    break;
                case 数字_特殊字符:
                    list.addAll(mNumberList);
                    list.addAll(mSpecialList);
                    break;
                case 大小写字母_数字_特殊字符:
                    list.addAll(mUppercaseList);
                    list.addAll(mLowercaseList);
                    list.addAll(mNumberList);
                    list.addAll(mSpecialList);
                    break;
            }
        } else {
            list.addAll(mUppercaseList);
            list.addAll(mLowercaseList);
            list.addAll(mNumberList);
        }
        return getRandomString(length, list);
    }

    private static String getRandomString(int length, List<String> letter) {
        int i;
        int count = 0;
        int maxNum = letter.size();
        StringBuffer pwd = new StringBuffer();
        Random r = new Random();
        while (count < length) {
            i = Math.abs(r.nextInt(maxNum));
            if (i >= 0 && i < letter.size()) {
                pwd.append(letter.get(i));
                count++;
            }
        }
        return pwd.toString();
    }
}
