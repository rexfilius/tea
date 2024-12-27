package github.com.rexfilius.tea.modules.category.repository;

import github.com.rexfilius.tea.modules.category.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
