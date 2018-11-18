
use std::cmp::{min,max};


#[derive(Debug)]
pub struct Cursor {
    pub position: Option<usize>,
    max_position: Option<usize>,
}
impl Cursor {
    pub fn new(coll_len:usize) -> Cursor {
        Cursor {
            position: None,
            max_position: if coll_len == 0  { None } else { Some(coll_len - 1) } ,
        }
    }
    pub fn move_down(self) -> Cursor {
        if self.max_position.is_none() {
            self
        } else if let Some(x) = self.position {
            Cursor { position: Some(min(x+1,self.max_position.unwrap())), ..self }
        } else {
            Cursor { position: Some(0), ..self }
        }
    }
    pub fn move_up(self) -> Cursor {
        if self.max_position.is_none() {
            self
        } else if let Some(x) = self.position {
            Cursor { position: Some(max(x-1,0)), ..self }
        } else {
            Cursor { position: Some(self.max_position.unwrap()), ..self }
        }
    }
    pub fn change_coll(self, coll_len:usize) -> Cursor {
        let new_pos = if self.position.filter(|&x| x < coll_len).is_some() { self.position } else { None };
        let nms = if coll_len > 0 { Some(coll_len) } else { None };
        Cursor { position: new_pos, max_position: nms }
    }
}

#[cfg(test)]
mod cursor_tests {
    use cursor::Cursor;
    #[test]
    fn can_create() {
        let c = Cursor::new(10);
        assert_eq!(c.max_position.unwrap(), 9);
        assert!(c.position.is_none());
    }
    fn is_within_bounds(c: &Cursor) -> bool {
        c.position.is_none() || c.position.unwrap() <= c.max_position.unwrap() 
    }
    #[test]
    fn can_move_around_within_bounds() {
        let mut c = Cursor::new(10);
        for _ in 1..100 {
            c = c.move_down();
            assert!(is_within_bounds(&c)) 
        }
        for _ in 1..30 {
            c = c.move_down();
            assert!(is_within_bounds(&c)) 
        }
        for _ in 1..5 {
            c = c.move_down();
            assert!(is_within_bounds(&c)) 
        }
    }
}
