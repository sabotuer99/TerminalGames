package whorten.termgames.games.tableflipper.board.player;

import java.util.TreeSet;

import whorten.termgames.entity.AbstractEntityState;
import whorten.termgames.geometry.Coord;
import whorten.termgames.glyphs.Glyph;

public class PlayerState extends AbstractEntityState<PlayerState> {
	
	private static PlayerState base_flipLeft = new PlayerState(PlayerStrings.THROW_LEFT);
	private static PlayerState base_flipRight = new PlayerState(PlayerStrings.THROW_RIGHT);
	private static PlayerState base_doubleFlip = new PlayerState(PlayerStrings.THROW_BOTH);
	private static PlayerState base_up = new PlayerState(PlayerStrings.WALK_UP);
	private static PlayerState base_down = new PlayerState(PlayerStrings.WALK_DOWN);
	private static PlayerState base_left = new PlayerState(PlayerStrings.WALK_LEFT);
	private static PlayerState base_right = new PlayerState(PlayerStrings.WALK_RIGHT);
	private static PlayerState base_stand = new PlayerState(PlayerStrings.STAND_STILL);
	static {
		base_left.flipNothing = base_flipLeft;
		base_right.flipNothing = base_flipRight;
		base_up.flipNothing = base_doubleFlip;
		base_down.flipNothing = base_doubleFlip;
		base_flipLeft.flipNothing = base_flipLeft;
		base_flipRight.flipNothing = base_flipRight;
		base_doubleFlip.flipNothing = base_doubleFlip;
		base_stand.flipNothing = base_doubleFlip;
		addBaseStates(base_flipLeft);
		addBaseStates(base_flipRight);
		addBaseStates(base_doubleFlip);
		addBaseStates(base_up);
		addBaseStates(base_down);
		addBaseStates(base_left);
		addBaseStates(base_right);
		addBaseStates(base_stand);
	}
	
	public static PlayerState getStartState(Coord baseCoord){
		PlayerState ps = new PlayerState(base_stand);
		ps.stand = base_stand;
		ps.coords = new TreeSet<Coord>();
		ps.coords.add(baseCoord);
		for(int i = 1; i < 7; i++){
			ps.coords.add(Coord.right(baseCoord, i));
		}
		return ps;
	}

	private static void addBaseStates(PlayerState base) {
		base.up = base_up;
		base.down = base_down;
		base.left = base_left;
		base.right = base_right;
		base.flipRight = base_flipRight;
		base.flipLeft = base_flipLeft;
		base.doubleFlip = base_doubleFlip;
	}
	private PlayerState(){};
	private PlayerState(String baseString){
		this.baseString = baseString;
	}
	private PlayerState(PlayerState base){
		this.baseString  = base.baseString;
		this.coords      = new TreeSet<>(base.coords);
		this.stand       = base.stand;      
		this.up          = base.up;         
		this.down        = base.down;       
		this.left        = base.left;       
		this.right       = base.right;      
		this.flipLeft    = base.flipLeft;   
		this.flipRight   = base.flipRight;  
		this.doubleFlip  = base.doubleFlip; 
		this.flipNothing = base.flipNothing;	
	}
	
	private String baseString;
	private PlayerState stand;
	private PlayerState up;
	private PlayerState down;
	private PlayerState left;
	private PlayerState right;
	private PlayerState flipLeft;
	private PlayerState flipRight;
	private PlayerState doubleFlip;
	private PlayerState flipNothing;

	@Override
	public PlayerState moveUp() {
		PlayerState up = new PlayerState(this.up);
		up.coords = new TreeSet<>(Coord.allUp(this.coords, 1));
		up.stand = base_stand;
		return up;
	}

	@Override
	public PlayerState moveDown() {
		PlayerState down = new PlayerState(this.down);
		down.coords = new TreeSet<>(Coord.allDown(this.coords, 1));
		down.stand = base_stand;
		return down;
	}

	@Override
	public PlayerState moveLeft() {
		PlayerState left = new PlayerState(this.left);
		left.coords = new TreeSet<>(Coord.allLeft(this.coords, 2));
		left.stand = base_left;
		return left;
	}

	@Override
	public PlayerState moveRight() {
		PlayerState right = new PlayerState(this.right);
		right.coords = new TreeSet<>(Coord.allRight(this.coords, 2));
		right.stand = base_right;
		return right;
	}
	
	public PlayerState flipNothing(){
		PlayerState flipNothing = new PlayerState(this.flipNothing);
		flipNothing.coords = new TreeSet<>(this.coords);
		flipNothing.stand = this.stand;
		return flipNothing;
	}
	
	public PlayerState flipLeft(){
		PlayerState flipLeft = new PlayerState(this.flipLeft);
		flipLeft.coords = new TreeSet<>(this.coords);
		flipLeft.stand = this.stand;
		return flipLeft;
	}
	
	public PlayerState flipRight(){
		PlayerState flipRight = new PlayerState(this.flipRight);
		flipRight.coords = new TreeSet<>(this.coords);
		flipRight.stand = this.stand;
		return flipRight;
	}
	
	public PlayerState doubleFlip(){
		PlayerState doubleFlip = new PlayerState(this.doubleFlip);
		doubleFlip.coords = new TreeSet<>(this.coords);
		doubleFlip.stand = this.stand;
		return doubleFlip;
	}
	
	public PlayerState stand(){
		PlayerState stand = new PlayerState(this.stand);
		stand.coords = new TreeSet<>(this.coords);
		stand.stand = stand;
		return stand;
	}

	@Override
	public String getBaseString() {
		return baseString;
	}

	@Override
	public Coord getBaseCoord() {
		return coords.iterator().next();
	}

	@Override
	public Glyph getBaseGlyph() {
		return new Glyph.Builder(" ").build();
	}
	
	@Override
	public String toString() {
		return String.format("PlayerState: baseString:[%s] baseCoord:[%s] baseGlyph:[%s]",
				getBaseString(),
				getBaseCoord(),
				getBaseGlyph());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((baseString == null) ? 0 : baseString.hashCode());
		result = prime * result + ((getBaseCoord() == null) ? 0 : getBaseCoord().hashCode());
		result = prime * result + ((getBaseGlyph() == null) ? 0 : getBaseGlyph().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlayerState other = (PlayerState) obj;
		return (this.baseString.equals(other.baseString) 
		     && this.getBaseCoord().equals(other.getBaseCoord())
		     && this.getBaseGlyph().equals(other.getBaseGlyph()));
	}
	


}
