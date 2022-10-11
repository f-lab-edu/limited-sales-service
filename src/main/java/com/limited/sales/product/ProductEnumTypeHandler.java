package com.limited.sales.product;

import com.limited.sales.product.vo.Product;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.stereotype.Component;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ProductEnumTypeHandler implements TypeHandler<Product.SalesStatus> {

  /**
   * @param ps
   * @param i
   * @param parameter
   * @param jdbcType
   * @throws SQLException
   */
  @Override
  public void setParameter(
      PreparedStatement ps, int i, Product.SalesStatus parameter, JdbcType jdbcType)
      throws SQLException {
    ps.setString(i, parameter.getStatus());
  }

  /**
   * @param rs the rs
   * @param columnName Column name, when configuration <code>useColumnLabel</code> is <code>false
   *     </code>
   * @return
   * @throws SQLException
   */
  @Override
  public Product.SalesStatus getResult(ResultSet rs, String columnName) throws SQLException {
    return getProductSalesType(rs.getString(columnName));
  }

  /**
   * @param rs
   * @param columnIndex
   * @return
   * @throws SQLException
   */
  @Override
  public Product.SalesStatus getResult(ResultSet rs, int columnIndex) throws SQLException {
    return getProductSalesType(rs.getString(columnIndex));
  }

  /**
   * @param cs
   * @param columnIndex
   * @return
   * @throws SQLException
   */
  @Override
  public Product.SalesStatus getResult(CallableStatement cs, int columnIndex) throws SQLException {
    return getProductSalesType(cs.getString(columnIndex));
  }

  private Product.SalesStatus getProductSalesType(final String status) {
    for (Product.SalesStatus type : Product.SalesStatus.values()) {
      if (type.getStatus().equals(status)) {
        return type;
      }
    }
    throw new RuntimeException("상품 상태가 존재하지 않습니다.");
  }
}
