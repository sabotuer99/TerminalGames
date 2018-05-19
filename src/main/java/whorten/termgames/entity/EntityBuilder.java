package whorten.termgames.entity;

public interface EntityBuilder<K extends Entity, 
						       S extends EntityState<S>,
						       B extends EntityBuilder<K,S,B>> {

	B withState(S state);
	K build();
}
