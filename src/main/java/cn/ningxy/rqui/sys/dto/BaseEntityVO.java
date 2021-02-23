package cn.ningxy.rqui.sys.dto;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ningxy
 */
public abstract class BaseEntityVO<V extends BaseEntityVO<V, E>, E> {

    /**
     * Entity -> VO
     *
     * @param entity Entity
     * @return VO
     */
    public abstract V fromEntity(final E entity);

    /**
     * VO -> Entity
     *
     * @param vo VO
     * @return Entity
     */
    public abstract E toEntity();

    /**
     * Entity (Collection) -> VO
     *
     * @param entities Entity Collection
     * @return VO List
     */
    public final List<V> fromEntities(final Collection<E> entities) {
        return entities.stream().map(this::fromEntity).collect(Collectors.toList());
    }

    /**
     * VO (Collection) -> Entity
     *
     * @param vos VO Collection
     * @return Entity List
     */
    public final List<E> toEntities(final Collection<V> vos) {
        return vos.stream().map(BaseEntityVO::toEntity).collect(Collectors.toList());
    }
}
