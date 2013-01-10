package devhood;

import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import devhood.dao.DataDao;
import devhood.model.Data;

@ContextConfiguration(locations = "classpath:/app-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DataDaoTest {

	@Resource
	private DataDao underTest;

	@PersistenceContext
	private EntityManager em;

	@Before
	public void setup() {

	}

	@Test
	@Transactional
	public void testSave_and_version_andfind() {
		Data d = new Data();
		d.setGivenname("test3");
		d.setName("test3");

		underTest.save(d);

		d.setName("test4");
		underTest.save(d);

		List list = AuditReaderFactory.get(em).createQuery()
				.forRevisionsOfEntity(Data.class, false, true).getResultList();

		list = AuditReaderFactory.get(em).createQuery()
				.forRevisionsOfEntity(Data.class, false, true)
				.add(AuditEntity.property("name").eq("test3")).getResultList();

	}

	@Test
	public void testSave_and_version() {
		Data d = new Data();
		d.setGivenname("test3");
		d.setName("test3");

		underTest.save(d);
		// Hibernate: insert into Data (givenname, name) values ('test3',
		// 'test3')
		// Hibernate: insert into REVINFO (REVTSTMP) values (1357846651458)
		// Hibernate: insert into Data_AUD (REVTYPE, givenname, name, id, REV)
		// values (0, 'test3', 'test3', 1, 1)

		d.setName("test4");
		underTest.save(d);
		// Hibernate: update Data set givenname='test3', name='test4' where id=1
		// Hibernate: insert into REVINFO (REVTSTMP) values (1357846777672)
		// Hibernate: insert into Data_AUD (REVTYPE, givenname, name, id, REV)
		// values (1, 'test3', 'test4', 1 , 2)

		underTest.delete(d);
		// Hibernate: delete from Data where id=1
		// Hibernate: insert into REVINFO (REVTSTMP) values (1357848098690)
		// Hibernate: insert into Data_AUD (REVTYPE, givenname, name, id, REV)
		// values (2, NULL, NULL, 1, 3)

	}

}
