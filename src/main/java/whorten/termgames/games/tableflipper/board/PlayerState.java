package whorten.termgames.games.tableflipper.board;

import java.util.HashSet;
import java.util.Set;

import whorten.termgames.entity.AbstractEntityState;
import whorten.termgames.entity.EntityState;
import whorten.termgames.geometry.Coord;

public class PlayerState extends AbstractEntityState {
	
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
		PlayerState ps = new PlayerState(PlayerStrings.STAND_STILL);
		return null;
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
		this.coords      = new HashSet<>(base.coords);
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
	private EntityState up;
	private EntityState down;
	private EntityState left;
	private EntityState right;
	private PlayerState flipLeft;
	private PlayerState flipRight;
	private PlayerState doubleFlip;
	private PlayerState flipNothing;

	@Override
	public EntityState moveUp() {
		return up;
	}

	@Override
	public EntityState moveDown() {
		return down;
	}

	@Override
	public EntityState moveLeft() {
		return left;
	}

	@Override
	public EntityState moveRight() {
		// TODO Auto-generated method stub
		return right;
	}
	
	public PlayerState flipNothing(){
		return flipNothing;
	}
	
	public PlayerState flipLeft(){
		return flipLeft;
	}
	
	public PlayerState flipRight(){
		return flipRight;
	}
	
	public PlayerState doubleFlip(){
		return doubleFlip;
	}
	
	public PlayerState stand(){
		return stand;
	}

}
