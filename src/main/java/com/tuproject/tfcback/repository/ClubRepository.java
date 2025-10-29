package com.tuproject.tfcback.repository;

import com.tuproject.tfcback.dto.ClubDTO;
import com.tuproject.tfcback.model.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club, Long> {

	@Query("""
			   SELECT new com.tuproject.tfcback.dto.ClubDTO(
			     c.id, c.nombre, c.direccion, c.telefono, c.lat, c.lng
			   )
			   FROM Club c
			   ORDER BY c.nombre ASC
			""")
			List<ClubDTO> findAllDto();
}
