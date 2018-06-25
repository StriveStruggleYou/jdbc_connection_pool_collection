package org.ssy.mybatis;

import java.io.StringReader;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;


/**
 * @author yuechang yuechang5@hotmail.com
 * @ClassName: JsqlparserDemo
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @date 2015年9月2日 上午11:40:59
 */
public class JsqlparserDemo {


  public static void main(String[] args) throws JSQLParserException {
    demo();
  }

  public static void demo() throws JSQLParserException {
    CCJSqlParserManager pm = new CCJSqlParserManager();

    String sql =
        "SELECT name,age FROM MY_TABLE1, MY_TABLE2, (SELECT * FROM MY_TABLE3) LEFT OUTER JOIN MY_TABLE4 "
            + " WHERE ID = 1 AND ID2 IN (SELECT * FROM MY_TABLE6)";

    Statement statement = pm.parse(new StringReader(sql));

    Select selectStatement = (Select) statement;

    PlainSelect plainSelect = (PlainSelect) selectStatement.getSelectBody();

    Expression where = plainSelect.getWhere();
    //初始化接收获得到的字段信息
    StringBuffer allColumnNames = new StringBuffer();
    //BinaryExpression包括了整个where条件，
    //例如：AndExpression/LikeExpression/OldOracleJoinBinaryExpression
    if (where instanceof BinaryExpression) {
      allColumnNames = getColumnName((BinaryExpression) (where), allColumnNames);
      System.out.println("allColumnNames:" + allColumnNames.toString());
      allColumnNames = getColumnName2((BinaryExpression) (where), allColumnNames);

      System.out.println("allColumnNames:" + allColumnNames.toString());

    }



  }

  /**
   * 获得where条件字段中列名，以及对应的操作符
   *
   * @param @param expression
   * @param @param allColumnNames
   * @param @return 设定文件
   * @return StringBuffer 返回类型
   * @Title: getColumnName
   * @Description: TODO(这里用一句话描述这个方法的作用)
   */
  private static StringBuffer getColumnName(Expression expression, StringBuffer allColumnNames) {

    String columnName = null;
    if (expression instanceof BinaryExpression) {
      //获得左边表达式
      Expression leftExpression = ((BinaryExpression) expression).getLeftExpression();
      //如果左边表达式为Column对象，则直接获得列名
      if (leftExpression instanceof Column) {
        //获得列名
        columnName = ((Column) leftExpression).getColumnName();
        allColumnNames.append(columnName);
        allColumnNames.append(":");
        //拼接操作符
        allColumnNames.append(((BinaryExpression) expression).getStringExpression());
        //allColumnNames.append("-");
      }
      //否则，进行迭代
      else if (leftExpression instanceof BinaryExpression) {
        getColumnName((BinaryExpression) leftExpression, allColumnNames);
      }
    }
    return allColumnNames;
  }
  private static StringBuffer getColumnName2(Expression expression, StringBuffer allColumnNames){

    String columnName = null;

    //获得右边表达式，并分解
    Expression rightExpression = ((BinaryExpression) expression).getRightExpression();
    if (rightExpression instanceof BinaryExpression) {
      Expression leftExpression2 = ((BinaryExpression) rightExpression).getLeftExpression();
      if (leftExpression2 instanceof Column) {
        //获得列名
        columnName = ((Column) leftExpression2).getColumnName();
        allColumnNames.append("-");
        allColumnNames.append(columnName);
        allColumnNames.append(":");
        //获得操作符
        allColumnNames.append(((BinaryExpression) rightExpression).getStringExpression());
      }
    }
    return allColumnNames;
  }




}

