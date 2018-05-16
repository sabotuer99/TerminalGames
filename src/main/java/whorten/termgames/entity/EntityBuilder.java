package whorten.termgames.entity;

public interface EntityBuilder {

	Entity build();
	EntityBuilder toBuilder(Entity entity);
	EntityBuilder withState(EntityState state);
}
