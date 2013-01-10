package devhood.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import devhood.model.Data;

public interface DataDao extends JpaRepository<Data, Long> {

}
