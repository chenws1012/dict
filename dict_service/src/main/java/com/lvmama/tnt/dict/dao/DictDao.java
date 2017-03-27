package com.lvmama.tnt.dict.dao;

import com.lvmama.tnt.dict.dto.Page;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * Created by chenwenshun on 2016/12/30.
 */
@Repository
public class DictDao extends HibernateDaoSupport{

    @Autowired
    public void settSessionFactory(SessionFactory sessionFactory){
        super.setSessionFactory(sessionFactory);

    }

    public <T> T findById(Class<T> type, long ids) {
        return (T) getSessionFactory().getCurrentSession().get(type, ids);
//        return (T) getHibernateTemplate().get(type, ids);
    }

    public <T> T queryUniqueByParam(Class<T> type, String attrName, Object value) {
        // TODO Auto-generated method stub
        String hql = "FROM " + type.getSimpleName() + " t WHERE t." + attrName + " =:value";
        List<T> list = (List<T>) getHibernateTemplate().findByNamedParam(hql, "value", value);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }


    public <T> List<T> queryByParameter(String hql, Map<String, Object> map, boolean cacheable) {
        Query query = this.createQuery(hql, map, cacheable);
        return query.list();
    }

    public <T> T findUnique(final String hql, final Map<String, Object> values) {
        return (T) createQuery(hql, values ,false).uniqueResult();
    }

    public <T> T merge(T entity) {
        entity = (T) this.getSessionFactory().getCurrentSession().merge(entity);
        getSessionFactory().getCurrentSession().flush();
        return entity;
    }

    public <T> T save(T entity) {
        this.getSessionFactory().getCurrentSession().saveOrUpdate(entity);
        return entity;
    }

    public <T> void deleteById(Class<T> type, final long id) {
        final String hql = "DELETE FROM " + type.getSimpleName() + " c WHERE c.id=:id";
        Query q = this.getSessionFactory().getCurrentSession().createQuery(hql);
        q.setParameter("id",id);
        q.executeUpdate();
//        this.getHibernateTemplate().execute(new HibernateCallback() {
//
//            public Object doInHibernate(Session session) throws HibernateException {
//                Query q = session.createQuery(hql);
//                q.setParameter("id", id);
//                return q.executeUpdate();
//            }
//        });
    }

    public <T> Page<T> findPage(final Page<T> page, final String hql, final Map<String, Object> values) {
        Assert.notNull(page, "page不能为空");
        Query q = createQuery(hql, values,false);

        if (page.isAutoCount()) {
            long totalCount = countHqlResult(hql, values);
            page.setTotalCount(totalCount);
        }

        setPageParameter(q, page);
        List result = q.list();
        page.setResult(result);
        return page;
    }

    /**
     * 设置分页参数到Query对象,辅助函数.
     */
    private <T> Query setPageParameter(final Query q, final Page<T> page) {
        // hibernate的firstResult的序号从0开始
        q.setFirstResult(page.getFirst() - 1);
        q.setMaxResults(page.getPageSize());
        return q;
    }

    public long countHqlResult(final String hql, final Map<String, Object> values) {
        String fromHql = hql;
        // select子句与order by子句会影响count查询,进行简单的排除.
        fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
        fromHql = StringUtils.substringBefore(fromHql, "order by");

        String countHql = "select count(*) " + fromHql;

        try {
            Long count = findUnique(countHql, values);
            return count;
        } catch (Exception e) {
            throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
        }
    }


    protected Query createQuery(String hql, Map<String, Object> map, boolean cacheable) {
        Query query = this.getSessionFactory().getCurrentSession().createQuery(hql);
        if (map == null) {
            return query;
        }
        // Iterator<String> keys = map.keySet().iterator();
        // while(keys.hasNext()){
        // String key = keys.next();
        // Object values = map.get(key);
        // query.setParameter(key, values);
        // }
        query.setCacheable(cacheable);
        if (map != null) {
            query.setProperties(map);
        }
        return query;
    }

}
