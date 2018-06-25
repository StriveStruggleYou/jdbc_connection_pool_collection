package org.ssy.mybatis;

import java.util.ArrayList;
import java.util.List;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;

/**
 * Created by manager on 2018/6/25.
 */
public class LearnJSqlParser {

  public static void main(String args[]) {

    String sql =
        "SELECT name,age FROM MY_TABLE1, MY_TABLE2, (SELECT * FROM MY_TABLE3) LEFT OUTER JOIN MY_TABLE4 "
            + " WHERE ID = 1 AND ID2 = (SELECT * FROM MY_TABLE6)";
    parse(sql);
  }

  public static void parse(String sql) {
    try {
      Statement statement = CCJSqlParserUtil.parse(sql);

      Select selectStatement = (Select) statement;
      TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
      List<String> tableList = tablesNamesFinder.getTableList(selectStatement);
      System.out.println("sql:" + sql);
      System.out.println("useTable:" + tableList.toString());
      PlainSelect plainSelect = (PlainSelect) selectStatement.getSelectBody();
      Expression where = plainSelect.getWhere();
      //初始化接收获得到的字段信息
      List<String> queryName = new ArrayList<>();
      //BinaryExpression包括了整个where条件，
      //例如：AndExpression/LikeExpression/OldOracleJoinBinaryExpression
      if (where instanceof BinaryExpression) {
        getColumnName((BinaryExpression) (where), queryName);
        System.out.println("queryNames:" + queryName.toString());
      }
      System.out.println("selectItems" + plainSelect.getSelectItems().toString());
    } catch (JSQLParserException e) {
      e.printStackTrace();
    }
  }


  private static void getColumnName(Expression expression, List<String> queryName) {
    String columnName = null;
    if (expression instanceof BinaryExpression) {
      //获得左边表达式
      Expression leftExpression = ((BinaryExpression) expression).getLeftExpression();
      //如果左边表达式为Column对象，则直接获得列名
      if (leftExpression instanceof Column) {
        //获得列名
        columnName = ((Column) leftExpression).getColumnName();
        queryName.add(columnName);
      }
      //否则，进行迭代
      else if (leftExpression instanceof BinaryExpression) {
        getColumnName((BinaryExpression) leftExpression, queryName);
      }
      //获得右边表达式，并分解
      Expression rightExpression = ((BinaryExpression) expression).getRightExpression();
      if (rightExpression instanceof BinaryExpression) {
        Expression leftExpression2 = ((BinaryExpression) rightExpression).getLeftExpression();
        if (leftExpression2 instanceof Column) {
          //获得列名
          columnName = ((Column) leftExpression2).getColumnName();
          queryName.add(columnName);
        }
      }
    }
  }
}



