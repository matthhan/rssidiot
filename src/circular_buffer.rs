
use can_replace_at_index::CanReplaceAtIndex;
#[derive(Debug,Serialize,Deserialize,PartialEq,Eq,Clone)]
pub struct CircularBuffer<T> where T:Clone {
    
    last_inserted: usize,
    backing_structure: Vec<T>,
    size:usize,
}
impl<T> CircularBuffer<T>  where T:Clone {
    pub fn new(size:usize) -> CircularBuffer<T> {
        CircularBuffer {
            size,
            last_inserted: size - 1,
            backing_structure: Vec::new(),
        }
    }
    pub fn add(self,thing: T) -> CircularBuffer<T> {
        let last_inserted_new = (self.last_inserted + 1) % self.size;
        let backing_structure_new = self.backing_structure.replace_at_index(last_inserted_new,&|_| thing.clone());
        CircularBuffer {
            size: self.size,
            last_inserted: last_inserted_new,
            backing_structure: backing_structure_new,
        }
    }
    pub fn len(&self) -> usize {
        self.backing_structure.len()
    }
    pub fn iter<'a>(&'a self) -> CircularBufferIterator<'a,T> {
        let bs_len = self.backing_structure.len();
        let res = if bs_len != 0 {
            CircularBufferIterator::new(self,(self.last_inserted + 1) % bs_len)
        } else {
            CircularBufferIterator::empty(self)
        };
        res
    }
}
pub struct CircularBufferIterator<'a, T:'a> where T:Clone {
    buf: &'a CircularBuffer<T>,
    i: usize,
    counter: usize,
    done_iterating: bool,
}
impl<'a, T> CircularBufferIterator<'a, T> where T:Clone {
    fn new(buf:&'a CircularBuffer<T>,i:usize) -> CircularBufferIterator<'a, T> {
        CircularBufferIterator {
            i:i,
            buf:buf,
            counter: 0,
            done_iterating: false,
        }
    }
    fn empty(buf:&'a CircularBuffer<T>) -> CircularBufferIterator<'a, T> {
        CircularBufferIterator {
            i:0,
            buf: buf,
            counter: 0,
            done_iterating: true,
        }

    }
}
impl<'a, T> Iterator for CircularBufferIterator<'a, T> where T:Clone {
    type Item = T;
    fn next(&mut self) -> Option<T> {
        if !self.done_iterating {
            let res = Some(self.buf.backing_structure[self.i].clone());
            self.counter += 1;
            self.done_iterating = self.i == self.buf.last_inserted || (self.counter == self.buf.backing_structure.len());
            self.i = (self.i + 1) % self.buf.backing_structure.len();
            res
        } else {
            None
        }
    }
}

impl<T> CanReplaceAtIndex<T> for CircularBuffer<T> where T:Clone {
    fn replace_at_index(&self,ind:usize,f:&Fn(Option<T>) -> T) -> CircularBuffer<T> {
        CircularBuffer { backing_structure: self.backing_structure.replace_at_index(ind,f), ..*self }
    }
}
#[cfg(test)]
mod circular_buffer_tests {
    use circular_buffer::CircularBuffer;
    extern crate serde_json;
    #[test]
    fn can_iterate() {
        let buf:CircularBuffer<i32> = CircularBuffer::new(100);
        for _ in buf.iter() {
            assert!(true)
        }
    }
    #[test]
    fn can_add() {
        let mut buf:CircularBuffer<i32> = CircularBuffer::new(30);
        for i in 1..105 {
            buf  = buf.add(i);
        }
        assert!(buf.len() > 0)
    }
    #[test]
    fn iterates_from_oldest() {
        let mut buf:CircularBuffer<i32> = CircularBuffer::new(10);
        for i in 1..105 {
            buf  = buf.add(i);
        }
        let mut it = buf.iter();
        for i in 95..105 {
            match it.next() {
                Some(v) => assert_eq!(v, i),
                None => assert!(false)
            }
        }
        
    }
    #[test]
    fn can_be_serialized_and_deserialized_correctly() {
        let mut buf:CircularBuffer<i32> = CircularBuffer::new(10);
        for i in 1..105 {
            buf  = buf.add(i);
        }
        let as_str = serde_json::to_string(&buf).unwrap();
        let from_str:CircularBuffer<i32> = serde_json::from_str(&as_str).unwrap();
        assert_eq!(buf,from_str);

    }
    #[test]
    fn can_iter_twice() {
        let mut buf:CircularBuffer<i32> = CircularBuffer::new(30);
        for i in 1..105 {
            buf  = buf.add(i);
        }
        for x in buf.iter() {
            assert!(true);
        }
        for x in buf.iter() {
            assert!(true);
        }
        assert!(buf.len() > 0)
    }
}


