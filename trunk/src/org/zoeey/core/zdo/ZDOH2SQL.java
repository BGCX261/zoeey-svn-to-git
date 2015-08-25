/*
 * MoXie (SysTem128@GMail.Com) 2009-2-1 15:59:59
 * $Id: ZDOH2SQL.java 79 2010-05-14 09:24:37Z MoXie $
 *
 * Copyright &copy; 2008-2009 Zoeey.Org
 * Code license: GNU Lesser General Public License Version 3
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 */
package org.zoeey.core.zdo;

import org.zoeey.core.zdo.zdbcontrol.ZDBControlNamed;
import org.zoeey.core.zdo.zdbcontrol.ZDBControlNamedAble;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import org.zoeey.core.resource.ResourceExceptionMsg;
import org.zoeey.core.zdo.exceptions.ZDOException;

/**
 *
 * @author MoXie(SysTem128@GMail.Com)
 */
public class ZDOH2SQL implements ZDOAble {

    /**
     * 链接
     */
    private Connection conn = null;
    /**
     *
     */
    private ZDBControlNamedAble zdb = null;
    /**
     * 生成的SQL语句
     */
    private String lastSql = null;
    /**
     * 当前装载的表
     */
    private TableItemAble table = null;
    private SQLHelper helper = null;

    /**
     * 初始化conn
     * @param conn
     */
    public ZDOH2SQL(Connection conn) {
        this.conn = conn;
        helper = new SQLHelper();
        zdb = new ZDBControlNamed(conn);
        helper.setQuoter(' ');
    }

    /**
     *
     * @param query
     * @return
     */
    public ZDOAble contact(String query) {
        return this;
    }

    /**
     *
     * @param table
     * @return
     */
    public ZDOAble bindParams(TableItemAble table) {
        return this;
    }

    /**
     *
     * @param table
     * @return
     */
    public ZDOAble contactTable(TableItemAble table) {
        this.table = table;
        return this;
    }

    /**
     *
     * @param tables
     * @return
     */
    public ZDOAble contactTables(TableItemAble[] tables) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 自动生成语句
     * @return
     */
    private ZDOAble autoTpl(FieldItem[] entrys) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * <pre>
     * 使用模板创建查询
     * 例如： SELECT `id`,`title`,`content` FROM `article` WHERE `id` = :id
     * id为参数
     * 重复绑定参数无效
     * </pre>
     *
     * @param sqlTpl
     * @return
     */
    public ZDOAble useTpl(String sqlTpl) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @return
     */
    public String getLastSql() {
        return lastSql;
    }

    /**
     *
     * @param prefix
     * @return
     * @throws SQLException
     */
    public int insert(String prefix) throws SQLException {
        StringBuffer strBuffer = new StringBuffer(prefix);
        int rowCount = 0;
        if (table != null) {
            // INSERT INTO T_Article (
            FieldItem primeKey = table.getPrimeKey();
            strBuffer.append("INSERT INTO ")//
                    .append(table.getNickName()).append(" ( ");
            if (primeKey != null) {
                strBuffer.append(' ').append(table.getPrimeKey().getArgName()).append(" , "); // bug 只有prime键时不需要"，"。
            }
            strBuffer.append(helper.joinFields_Comma(table.getFields()));
            strBuffer.append(" ) VALUES ( "); //
            if (primeKey != null) {
                strBuffer.append(' ').append(table.getPrimeKey().getValue()).append(" , ");
            }
            strBuffer.append(helper.joinFields_ColonComma(table.getFields()));
            strBuffer.append(") ");
            lastSql = strBuffer.toString();
            zdb.prepareStatement(lastSql);
            {
                FieldItem[] fields = table.getFields();
                FieldItem field = null;
                for (int i = fields.length - 1; i >= 0; i--) {
                    field = fields[i];
                    if (!field.isIsActive()) {
                        continue;
                    }
                    if (field.getType() == Types.VARCHAR) {
                        zdb.setString(field.getArgName(), field.getValue().toString());
                    } else {
                        zdb.setInt(field.getArgName(), field.getValue().toInteger());
                    }
                }
            }
            rowCount += zdb.executeUpdate();
        } else {
            throw new ZDOException(ResourceExceptionMsg.ZDO_TABLEUNDEFINDED_EXCEPTION);
        }
        return rowCount;
    }

    public int insert() throws SQLException {
        return this.insert("");
    }

    public String lastInsertId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ResultSet read() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ResultSet readList() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int update() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int del() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
