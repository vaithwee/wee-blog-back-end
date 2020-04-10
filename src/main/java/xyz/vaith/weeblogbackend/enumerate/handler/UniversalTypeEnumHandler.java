package xyz.vaith.weeblogbackend.enumerate.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import xyz.vaith.weeblogbackend.enumerate.BaseEnum;
import xyz.vaith.weeblogbackend.enumerate.util.EnumUtils;
import xyz.vaith.weeblogbackend.enumerate.image.ImageAccessType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//数据库中的类型
@MappedJdbcTypes(value = {JdbcType.INTEGER, JdbcType.BIGINT})
//枚举类型
@MappedTypes(value = {ImageAccessType.class})
public class UniversalTypeEnumHandler<E extends Enum<E> & BaseEnum> extends BaseTypeHandler<E> {

    private final  Class<E> type;

    public UniversalTypeEnumHandler(Class<E> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, E e, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, e.getCode());
    }

    @Override
    public E getNullableResult(ResultSet resultSet, String s) throws SQLException {
        int code = resultSet.getInt(s);
        return resultSet.wasNull() ? null : EnumUtils.codeOf(type, code);
    }

    @Override
    public E getNullableResult(ResultSet resultSet, int i) throws SQLException {
        int code = resultSet.getInt(i);
        return resultSet.wasNull() ? null : EnumUtils.codeOf(type, code);
    }

    @Override
    public E getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        int code = callableStatement.getInt(i);
        return callableStatement.wasNull() ? null : EnumUtils.codeOf(type, code);
    }
}
