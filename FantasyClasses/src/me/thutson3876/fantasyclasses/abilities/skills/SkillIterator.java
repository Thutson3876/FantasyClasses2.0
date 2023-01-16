package me.thutson3876.fantasyclasses.abilities.skills;

import java.util.Iterator;

public class SkillIterator implements Iterator<Skill> {

	enum ProcessStages {
		ProcessParent, ProcessChildCurNode, ProcessChildSubNode
	}
	
	private Skill node;
	
	private ProcessStages doNext;
	private Skill next;
	private Iterator<Skill> childrenCurNodeIter;
	private Iterator<Skill> childrenSubNodeIter;
	
	public SkillIterator(Skill node) {
		this.node = node;
		this.doNext = ProcessStages.ProcessParent;
		this.childrenCurNodeIter = node.next.iterator();
	}
	
	@Override
	public boolean hasNext() {

		if (this.doNext == ProcessStages.ProcessParent) {
			this.next = this.node;
			this.doNext = ProcessStages.ProcessChildCurNode;
			return true;
		}

		if (this.doNext == ProcessStages.ProcessChildCurNode) {
			if (childrenCurNodeIter.hasNext()) {
				Skill childDirect = childrenCurNodeIter.next();
				childrenSubNodeIter = childDirect.iterator();
				this.doNext = ProcessStages.ProcessChildSubNode;
				return hasNext();
			}

			else {
				this.doNext = null;
				return false;
			}
		}
		
		if (this.doNext == ProcessStages.ProcessChildSubNode) {
			if (childrenSubNodeIter.hasNext()) {
				this.next = childrenSubNodeIter.next();
				return true;
			}
			else {
				this.next = null;
				this.doNext = ProcessStages.ProcessChildCurNode;
				return hasNext();
			}
		}

		return false;
	}

	@Override
	public Skill next() {
		return this.next;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}
}
