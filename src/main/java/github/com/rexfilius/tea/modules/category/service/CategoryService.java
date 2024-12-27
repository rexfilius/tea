package github.com.rexfilius.tea.modules.category.service;

import github.com.rexfilius.tea.exception.ResourceNotFoundException;
import github.com.rexfilius.tea.modules.category.model.Category;
import github.com.rexfilius.tea.modules.category.model.CategoryDto;
import github.com.rexfilius.tea.modules.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static github.com.rexfilius.tea.utils.ModelMapper.dtoToModel;
import static github.com.rexfilius.tea.utils.ModelMapper.modelToDto;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryDto addCategory(CategoryDto dto) {
        Category category = dtoToModel(dto);
        Category savedCategory = categoryRepository.save(category);
        return modelToDto(savedCategory);
    }

    public CategoryDto getCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        return modelToDto(category);
    }

    public List<CategoryDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map((category) -> modelToDto(category))
                .collect(Collectors.toList());
    }

    public CategoryDto updateCategory(CategoryDto dto, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        category.setName(dto.getName());
        category.setDescription(dto.getDescription());
        category.setId(dto.getId());

        Category updatedCategory = categoryRepository.save(category);
        return modelToDto(updatedCategory);
    }

    public void deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));
        categoryRepository.delete(category);
    }
}
