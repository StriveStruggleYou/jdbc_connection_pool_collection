package org.ssy.mybatis;

import java.util.ArrayList;
import java.util.List;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.AllComparisonExpression;
import net.sf.jsqlparser.expression.AnalyticExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.CastExpression;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.ExtractExpression;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.HexValue;
import net.sf.jsqlparser.expression.IntervalExpression;
import net.sf.jsqlparser.expression.JdbcNamedParameter;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.JsonExpression;
import net.sf.jsqlparser.expression.KeepExpression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.MySQLGroupConcat;
import net.sf.jsqlparser.expression.NotExpression;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.NumericBind;
import net.sf.jsqlparser.expression.OracleHierarchicalExpression;
import net.sf.jsqlparser.expression.OracleHint;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.RowConstructor;
import net.sf.jsqlparser.expression.SignedExpression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeKeyExpression;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.UserVariable;
import net.sf.jsqlparser.expression.ValueListExpression;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseLeftShift;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseRightShift;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Modulo;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.JsonOperator;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.expression.operators.relational.RegExpMatchOperator;
import net.sf.jsqlparser.expression.operators.relational.RegExpMySQLOperator;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SubSelect;
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



