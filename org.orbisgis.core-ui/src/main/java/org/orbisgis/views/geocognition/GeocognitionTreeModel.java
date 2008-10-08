package org.orbisgis.views.geocognition;

import java.util.ArrayList;

import javax.swing.JTree;
import javax.swing.tree.TreePath;

import org.orbisgis.Services;
import org.orbisgis.geocognition.Geocognition;
import org.orbisgis.geocognition.GeocognitionElement;
import org.orbisgis.geocognition.GeocognitionListener;
import org.orbisgis.ui.resourceTree.AbstractTreeModel;
import org.orbisgis.ui.resourceTree.FilterTreeModelDecorator;
import org.orbisgis.views.geocognition.filter.IGeocognitionFilter;

public class GeocognitionTreeModel extends FilterTreeModelDecorator {

	private ArrayList<IGeocognitionFilter> filters = new ArrayList<IGeocognitionFilter>();

	public GeocognitionTreeModel(JTree tree) {
		super(new GecognitionModel(tree), tree);

		Geocognition gc = Services.getService(Geocognition.class);
		gc.addGeocognitionListener(new GeocognitionListener() {

			@Override
			public void elementRemoved(Geocognition geocognition,
					GeocognitionElement element) {
				fireEvent(null);
			}

			@Override
			public void elementAdded(Geocognition geocognition,
					GeocognitionElement parent, GeocognitionElement newElement) {
				fireEvent(null);
			}

			@Override
			public boolean elementRemoving(Geocognition geocognition,
					GeocognitionElement element) {
				return true;
			}

			@Override
			public void elementMoved(Geocognition geocognition,
					GeocognitionElement element, GeocognitionElement oldParent) {
				fireEvent(null);
			}

		});
	}

	public void filter(String text, ArrayList<IGeocognitionFilter> filters) {
		this.filters = filters;
		filter(text.toLowerCase());
		fireEvent(null);
	}

	@Override
	protected boolean isFiltered() {
		return (super.isFiltered() || (this.filters.size() != 0));
	}

	@Override
	protected boolean isFiltered(Object elem) {
		GeocognitionElement element = (GeocognitionElement) elem;

		if (super.isFiltered(element)) {
			if (isLeaf(element)) {
				if (filters.size() > 0) {
					boolean anyMatches = false;
					for (IGeocognitionFilter filter : filters) {
						if (filter.accept(element.getTypeId())) {
							anyMatches = true;
							break;
						}
					}
					return anyMatches;
				} else {
					return true;
				}
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	private static class GecognitionModel extends AbstractTreeModel {
		private GecognitionModel(JTree tree) {
			super(tree);
		}

		@Override
		public Object getChild(Object parent, int index) {
			GeocognitionElement parentElement = (GeocognitionElement) parent;
			return parentElement.getElement(index);
		}

		@Override
		public int getChildCount(Object parent) {
			GeocognitionElement parentElement = (GeocognitionElement) parent;
			return parentElement.getElementCount();
		}

		@Override
		public int getIndexOfChild(Object parent, Object child) {
			GeocognitionElement parentElement = (GeocognitionElement) parent;
			for (int i = 0; i < parentElement.getElementCount(); i++) {
				if (parentElement.getElement(i) == child) {
					return i;
				}
			}

			return -1;
		}

		@Override
		public Object getRoot() {
			Geocognition gc = Services.getService(Geocognition.class);
			return gc.getRoot();
		}

		@Override
		public boolean isLeaf(Object node) {
			GeocognitionElement elem = (GeocognitionElement) node;
			return !elem.isFolder() || elem.getElementCount() == 0;
		}

		@Override
		public void valueForPathChanged(TreePath path, Object newValue) {
			// do nothing
		}
	}
}
