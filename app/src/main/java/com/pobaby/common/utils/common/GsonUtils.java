package com.pobaby.common.utils.common;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;

/**
 * Json 转换工具，主要是解决字段值为null的字段不解析的问题
 *
 * @author chenqh
 * created at 2019/6/19 9:23
 */
public class GsonUtils {

    /**
     * 此方法将指定对象序列化为等效的Json表示。
     * 当指定的对象不是泛型类型时，应使用此方法。 此方法使用
     * {@link ＃getClass（）}获取指定对象的类型，但是
     * {@code getClass（）}由于Type Erasure功能而丢失了泛型类型信息
     * Java。 请注意，如果任何对象字段是泛型类型，此方法可以正常工作，
     * 只是对象本身不应该是泛型类型。 如果对象是泛型类型，请使用
     * {@link #toJson（Object，Type）}代替。 如果你想把对象写出来
     * {@link Writer}，请改用{@link #toJson（Object，Appendable）}。
     *
     * @param src 为Gson创建Json表示的对象
     * @return Json代表{@code src}。
     */
    public static String toJson(Object src) {
        GsonBuilder gsonbuilder = new GsonBuilder().serializeNulls();
        return gsonbuilder.create().toJson(src);
    }

    /**
     * 此方法将指定的Json反序列化为指定类的对象。它不是
     * 如果指定的类是泛型类型，则适合使用，因为它不具有泛型
     * 由于Java的Type Erasure功能而输入类型信息。因此，这种方法不应该
     * 如果所需类型是泛型类型，则使用。请注意，如果使用此方法，此方法可以正常工作
     * 指定对象的字段是泛型，只是对象本身不应该是
     * 通用类型。对于对象是泛型类型的情况，请调用
     * {@link #fromJson（String，Type）}。如果您在{@link Reader}中使用Json而不是
     * 一个字符串，改为使用{@link #fromJson（Reader，Class）}。
     *
     * @param <T>      所需对象的类型
     * @param json     从中反序列化对象的字符串
     * @param classOfT 的T类
     *                    
     * @从字符串中返回类型为T的对象。如果{@code json}是{@code null}，则返回{@code null}。
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        GsonBuilder gsonbuilder = new GsonBuilder().serializeNulls();
        return gsonbuilder.create().fromJson(json, classOfT);
    }

    /**
     * 此方法将指定的Json反序列化为指定类型的对象。这种方法
     * 如果指定的对象是泛型类型，则非常有用。对于非通用对象，请使用
     * {@link #fromJson（String，Class）}代替。如果您在{@link Reader}中使用Json而不是
     * 一个字符串，改为使用{@link #fromJson（Reader，Type）}。
     *
     * @param <T>所需对象的类型
     * @param json       从中反序列化对象的字符串
     * @param typeOfT    src的特定通用类型。您可以使用以下方式获取此类型
     *                   {@link com.google.gson.reflect.TypeToken}类。例如，获取类型
     *                   {@code Collection <Foo>}，您应该使用：
     *                   <pre>
     *                   类型typeOfT = new TypeToken＆lt; Collection＆lt; Foo＆gt;＆gt;（）{}。getType（）;
     *                    </ pre>
     *                    @从字符串中返回类型为T的对象。如果{@code json}是{@code null}，则返回{@code null}。
     *                    @throws JsonParseException 如果json不是typeOfT类型的对象的有效表示
     *                    @throws JsonSyntaxException 如果json不是类型对象的有效表示
     *                      
     */
    public static <T> T fromJson(String json, Type typeOfT) {
        GsonBuilder gsonbuilder = new GsonBuilder().serializeNulls();
        return gsonbuilder.create().fromJson(json, typeOfT);
    }
}
