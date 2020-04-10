package xyz.vaith.weeblogbackend.enumerate;

public interface BaseEnum<E extends Enum<?>, T> {
    public Integer getCode();
    public String getText();
}
