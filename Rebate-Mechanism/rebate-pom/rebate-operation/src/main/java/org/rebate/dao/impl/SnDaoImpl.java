package org.rebate.dao.impl; 

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import org.rebate.dao.SnDao;
import org.rebate.entity.Sn;
import org.rebate.entity.Sn.Type;
import org.rebate.utils.FreemarkerUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import freemarker.template.TemplateException;

@Repository("snDaoImpl")
public class SnDaoImpl implements SnDao, InitializingBean {

	  private HiloOptimizer clearingHiloOptimizer;

	  @PersistenceContext
	  private EntityManager entityManager;
	  @Value("${sn.clearing.prefix}")
	  private String clearingPrefix;
	  @Value("${sn.clearing.maxLo}")
	  private int clearingMaxLo;

	  @Override
	  public String generate(Type type) {
	    Assert.notNull(type);
	    if (type == Type.SELLER_CLEARING_RECORD) {
	      return clearingHiloOptimizer.generate();
	    }
	    return null;
	  }

	  @Override
	  public void afterPropertiesSet() throws Exception {
		  clearingHiloOptimizer = new HiloOptimizer(Type.SELLER_CLEARING_RECORD, clearingPrefix, clearingMaxLo);

	  }

	  /**
	   * 高低位算法
	   */
	  private class HiloOptimizer {

	    private Type type;
	    private String prefix;
	    private int maxLo;
	    private int lo;
	    private long hi;
	    private long lastValue;

	    public HiloOptimizer(Type type, String prefix, int maxLo) {
	      this.type = type;
	      this.prefix = prefix != null ? prefix.replace("{", "${") : "";
	      this.maxLo = maxLo;
	      this.lo = maxLo + 1;
	    }

	    private long getLastValue(Type type) {
	      String jpql = "select sn from Sn sn where sn.type = :type";
	      Sn sn =
	          entityManager.createQuery(jpql, Sn.class).setFlushMode(FlushModeType.COMMIT)
	              .setLockMode(LockModeType.PESSIMISTIC_WRITE).setParameter("type", type)
	              .getSingleResult();
	      long lastValue = sn.getLastValue();
	      sn.setLastValue(lastValue + 1);
	      entityManager.merge(sn);
	      return lastValue;
	    }

	    public synchronized String generate() {
	      if (lo > maxLo) {
	        lastValue = getLastValue(type);
	        lo = lastValue == 0 ? 1 : 0;
	        hi = lastValue * (maxLo + 1);
	      }
	      try {
	        return FreemarkerUtils.process(prefix, null) + (hi + lo++);
	      } catch (IOException e) {
	        e.printStackTrace();
	      } catch (TemplateException e) {
	        e.printStackTrace();
	      }
	      return String.valueOf(hi + lo++);
	    }
	  }


	}
