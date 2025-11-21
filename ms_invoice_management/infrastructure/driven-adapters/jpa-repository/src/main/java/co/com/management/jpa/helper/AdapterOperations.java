package co.com.management.jpa.helper;

import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public abstract class AdapterOperations<E, D, I, R extends JpaRepository<D, I>> {

    protected R repository;
    private final Class<D> dataClass;
    protected ObjectMapper mapper;
    private final Function<D, E> toEntityFn;

    @SuppressWarnings("unchecked")
    protected AdapterOperations(R repository, ObjectMapper mapper, Function<D, E> toEntityFn) {
        this.repository = repository;
        this.mapper = mapper;
        ParameterizedType genericSuperclass = (ParameterizedType) this.getClass().getGenericSuperclass();
        this.dataClass = (Class<D>) genericSuperclass.getActualTypeArguments()[1];
        this.toEntityFn = toEntityFn;
    }

    protected D toData(E entity) {
        return mapper.map(entity, dataClass);
    }

    protected E toEntity(D data) {
        return data != null ? toEntityFn.apply(data) : null;
    }

    public E save(E entity) {
        return toEntity(repository.save(toData(entity)));
    }

    public E findById(I id) {
        return toEntity(repository.findById(id).orElse(null));
    }

    public List<E> findAll() {
        return repository.findAll().stream().map(this::toEntity).toList();
    }

    public List<E> findAll(Sort sort) {
        return repository.findAll(sort).stream().map(this::toEntity).toList();
    }

    public Page<E> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::toEntity);
    }

    public void deleteById(I id) {
        repository.deleteById(id);
    }

}