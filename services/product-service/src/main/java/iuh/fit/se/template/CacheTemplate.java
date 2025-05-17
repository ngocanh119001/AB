package iuh.fit.se.template;

import reactor.core.publisher.Mono;

public abstract class CacheTemplate<BASE, ID> {

	
	public Mono<BASE> findById(ID id) {
		return findByIdCache(id).switchIfEmpty(findByIdDb(id).flatMap(item -> saveCache(id, item).thenReturn(item)));
	}
	
	public Mono<BASE> save(ID id, BASE item) {
		return saveDb(id, item).then(deleteCache(id).thenReturn(item));
	}
	
	public Mono<Void> delete(ID id) {
		return deleteDb(id).then(deleteCache(id));
	}

	public abstract Mono<BASE> findByIdCache(ID id);

	public abstract Mono<BASE> findByIdDb(ID id);

	public abstract Mono<BASE> saveCache(ID id, BASE item);

	public abstract Mono<BASE> saveDb(ID id, BASE item);
	
	public abstract Mono<Void> deleteCache(ID id);
	
	public abstract Mono<Void> deleteDb(ID id);
}
