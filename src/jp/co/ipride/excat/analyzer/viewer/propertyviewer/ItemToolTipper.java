package jp.co.ipride.excat.analyzer.viewer.propertyviewer;

import jp.co.ipride.excat.common.ApplicationResource;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * ToolTips�N���X
 *
 * @author sai
 * @since 2009/10/21
 */
public class ItemToolTipper {
	/**
	 * ToolTip���܂܂��R���e�i
	 */
	private final TableViewer container;

	/**
	 * ���X�i�[
	 */
	private Listener toolTipListener;

	/**
	 * �R���X�g���N�^�[
	 *
	 * @param container ToolTip���܂܂��R���e�i
	 */
	public ItemToolTipper(TableViewer container) {
		if (container == null) {
			throw new IllegalArgumentException("Cannot be null: " + "container");
		}
		this.container = container;
		addToolTipListener();
	}

	/**
	 * ���X�i�[��ǉ����郁�\�b�h
	 */
	private void addToolTipListener() {
		// ���X�i�[��ǉ�����B
		toolTipListener = new Listener() {
			Shell tip = null;
			Text label = null;
			ViewerCell cell = null;

			public void handleEvent(Event event) {
				switch (event.type) {
				case SWT.Dispose:
					removeListeners();
				case SWT.KeyDown:
					if (tip != null && !tip.isDisposed()) {
						tip.dispose();
					}
					if (event.keyCode == SWT.F2) {
						Display display = container.getTable().getDisplay();
						if (cell != null) {
							String tooltipText = getToolTipText(cell);
							if (tooltipText != null && tooltipText.length() != 0) {
								// F2�L�[����������ꍇ�A�R�s�[�ł���ToolTip��\������B
								container.getTable().removeListener(SWT.MouseMove, toolTipListener);
								container.getTable().removeListener(SWT.MouseExit, toolTipListener);
								container.getTable().removeListener(SWT.MouseHover, toolTipListener);

								tip = new Shell(display, SWT.ON_TOP | SWT.RESIZE);
								tip.setLayout(new FillLayout());
								label = new Text(tip, SWT.LEFT | SWT.V_SCROLL| SWT.H_SCROLL | SWT.READ_ONLY);
								label.setText(tooltipText);
								label.setForeground(display
										.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
								label.setBackground(display
										.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
								Point pt = display.getCursorLocation();

								Rectangle displayBounds = display.getBounds();
								int left = pt.x;
								int top = pt.y + 20;
								int width = 300;
								int height = 110;
								if ((left + width) > displayBounds.width) {
									left = (width > displayBounds.width) ? 0
											: displayBounds.width - width;
								}
								if ((top + height) > displayBounds.height) {
									top = (height > displayBounds.height) ? 0
											: displayBounds.height - height;
								}
								tip.setBounds(left,top, width, height);
								tip.setVisible(true);

								tip.forceFocus();
								label.forceFocus();

								label.addListener(SWT.FocusOut, new Listener() {

									public void handleEvent(Event event) {
										if (tip != null && !tip.isDisposed()) {
											tip.dispose();
											tip = null;
											label = null;
											cell = null;
											addListeners();
										}
									}

								});
							}
						}
					}
					break;
				case SWT.MouseDown:
					if (tip != null) {
						if ((event.x < tip.getBounds().x || event.x > tip.getBounds().x+tip.getBounds().width)
								|| (event.y < tip.getBounds().y || event.y > tip.getBounds().y+tip.getBounds().height)) {
							// TooTip�ȊO�̏ꏊ���N���b�N����ƁAToolTip�����B
							tip.dispose();
							tip = null;
							label = null;
							cell = null;
							addListeners();
						}
					}
					break;
				case SWT.MouseMove:
				case SWT.MouseExit:
				case SWT.FocusOut:
					if (tip != null) {
						tip.dispose();
						tip = null;
						label = null;
						cell = null;
					}
					break;
				case SWT.MouseHover:
					if (tip != null && !tip.isDisposed()) {
						tip.dispose();
						cell = null;
					}

					// �}�E�X���R���g���[���̏�ɏd�˂�ꍇ�A�ʏ�ToolTip��\������B
					Display display = container.getTable().getDisplay();
					Point pt = new Point(event.x, event.y);
					cell = container.getCell(pt);

					if (cell != null && cell.getColumnIndex() > 0) {
						String tooltipText = getToolTipText(cell);
						if (tooltipText != null && tooltipText.length() != 0) {
							RowLayout layout = new RowLayout(SWT.VERTICAL);
							layout.spacing = 0;
							layout.marginBottom = 0;
							layout.marginLeft = 0;
							layout.marginRight = 0;
							layout.marginTop = 0;
							layout.fill = true;
							tip = new Shell(display, SWT.ON_TOP);
							tip.setLayout(layout);
							tip.setForeground(display
									.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
							tip.setBackground(display
									.getSystemColor(SWT.COLOR_INFO_BACKGROUND));

							RowData rowData = new RowData();
							rowData.height = 85;
							rowData.width = 300;
							label = new Text(tip, SWT.LEFT | SWT.MULTI);
							label.setText(tooltipText);
							label.setForeground(display
									.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
							label.setBackground(display
									.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
							label.setLayoutData(rowData);

							RowData rowData2 = new RowData();
							rowData2.height = 2;
							rowData.width = 300;
							Label separator = new Label(tip, SWT.HORIZONTAL | SWT.SEPARATOR);

							separator.setLayoutData(rowData2);

							RowData rowData3 = new RowData();
							rowData3.height = 15;
							rowData.width = 300;
							Text label2 = new Text(tip, SWT.RIGHT);
							label2.setText(ApplicationResource.getResource("Tooltip.Comment"));
							label2.setForeground(display
									.getSystemColor(SWT.COLOR_DARK_GRAY));
							label2.setBackground(display
									.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
							label2.setLayoutData(rowData3);

							pt = display.getCursorLocation();

							Rectangle displayBounds = display.getBounds();
							int left = pt.x;
							int top = pt.y + 20;
							int width = 300;
							int height = 110;
							if ((left + width) > displayBounds.width) {
								left = (width > displayBounds.width) ? 0
										: displayBounds.width - width;
							}
							if ((top + height) > displayBounds.height) {
								top = (height > displayBounds.height) ? 0
										: displayBounds.height - height;
								left = (pt.x - width - 20) < 0 ? pt.x + 20 : pt.x - width - 20;
							}
							tip.setBounds(left,top, width, height);
							tip.setVisible(true);
							container.getTable().forceFocus();
						}
					} else {
						cell = null;
					}
				}
			}
		};
		addListeners();
	}

	/**
	 * �e���X�i�[��ǉ����郁�\�b�h
	 */
	private void addListeners() {
		removeListeners();
		container.getTable().addListener(SWT.Dispose, toolTipListener);
		container.getTable().addListener(SWT.KeyDown, toolTipListener);
		container.getTable().addListener(SWT.MouseDown, toolTipListener);
		container.getTable().addListener(SWT.MouseExit, toolTipListener);
		container.getTable().addListener(SWT.MouseHover, toolTipListener);
		container.getTable().addListener(SWT.MouseMove, toolTipListener);
	}

	/**
	 * �e���X�i�[���폜���郁�\�b�h
	 */
	private void removeListeners() {
		container.getTable().removeListener(SWT.Dispose, toolTipListener);
		container.getTable().removeListener(SWT.KeyDown, toolTipListener);
		container.getTable().removeListener(SWT.MouseDown, toolTipListener);
		container.getTable().removeListener(SWT.MouseExit, toolTipListener);
		container.getTable().removeListener(SWT.MouseHover, toolTipListener);
		container.getTable().removeListener(SWT.MouseMove, toolTipListener);
	}

	/**
	 * ToolTip�̃e�L�X�g���擾���郁�\�b�h
	 *
	 * @param item ToolTip�̃e�L�X�g���擾����ViewerCell�I�u�W�F�N�g.
	 * @return ToolTip�̃e�L�X�g
	 */
	protected String getToolTipText(ViewerCell cell) {
		return cell.getText();
	}
}
