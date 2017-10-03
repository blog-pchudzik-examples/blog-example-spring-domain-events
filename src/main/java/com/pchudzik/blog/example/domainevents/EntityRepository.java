package com.pchudzik.blog.example.domainevents;

import org.springframework.data.jpa.repository.JpaRepository;

interface EntityRepository extends JpaRepository<AnyEntity, Long> {
}