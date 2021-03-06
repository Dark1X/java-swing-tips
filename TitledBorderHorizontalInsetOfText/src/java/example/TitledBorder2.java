// -*- mode:java; encoding:utf-8 -*-
// vim:set fileencoding=utf-8:
/*
 * Copyright (c) 1997, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package example;

import java.awt.*;
import java.awt.geom.Path2D;
import java.util.Objects;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicHTML;

/**
 * A class which implements an arbitrary border
 * with the addition of a String title in a
 * specified position and justification.
 *
 * <p>If the border, font, or color property values are not
 * specified in the constructor or by invoking the appropriate
 * set methods, the property values will be defined by the current
 * look and feel, using the following property names in the
 * Defaults Table:
 * <ul>
 * <li>&quot;TitledBorder.border&quot;
 * <li>&quot;TitledBorder.font&quot;
 * <li>&quot;TitledBorder.titleColor&quot;
 * </ul>
 *
 * <p><strong>Warning:</strong>
 * Serialized objects of this class will not be compatible with
 * future Swing releases. The current serialization support is
 * appropriate for short term storage or RMI between applications running
 * the same version of Swing.  As of 1.4, support for long term storage
 * of all JavaBeans&trade;
 * has been added to the <code>java.beans</code> package.
 * Please see {@link java.beans.XMLEncoder}.
 *
 * @author David Kloba
 * @author Amy Fowler
 */
@SuppressWarnings({"PMD.GodClass", "PMD.CyclomaticComplexity", "HiddenField"})
public class TitledBorder2 extends AbstractBorder {
  // @see javax/swing/border/TitledBorder.java
  private String title;
  private Border border;
  private int titlePosition;
  private int titleJustification;
  private Font titleFont;
  private Color titleColor;

  private final JLabel label;

  /**
   * Use the default vertical orientation for the title text.
   */
  public static final int DEFAULT_POSITION = 0;
  /** Position the title above the border's top line. */
  public static final int ABOVE_TOP = 1;
  /** Position the title in the middle of the border's top line. */
  public static final int TOP = 2;
  /** Position the title below the border's top line. */
  public static final int BELOW_TOP = 3;
  /** Position the title above the border's bottom line. */
  public static final int ABOVE_BOTTOM = 4;
  /** Position the title in the middle of the border's bottom line. */
  public static final int BOTTOM = 5;
  /** Position the title below the border's bottom line. */
  public static final int BELOW_BOTTOM = 6;

  /**
   * Use the default justification for the title text.
   */
  public static final int DEFAULT_JUSTIFICATION = 0;
  /** Position title text at the left side of the border line. */
  public static final int LEFT = 1;
  /** Position title text in the center of the border line. */
  public static final int CENTER = 2;
  /** Position title text at the right side of the border line. */
  public static final int RIGHT = 3;
  /** Position title text at the left side of the border line
   *  for left to right orientation, at the right side of the
   *  border line for right to left orientation.
   */
  public static final int LEADING = 4;
  /** Position title text at the right side of the border line
   *  for left to right orientation, at the left side of the
   *  border line for right to left orientation.
   */
  public static final int TRAILING = 5;

  // Space between the border and the component's edge
  protected static final int EDGE_SPACING = 2; // 2;

  // Space between the border and text
  protected static final int TEXT_SPACING = 5; // 2;

  // Horizontal inset of text that is left or right justified
  protected static final int TEXT_INSET_H = 10; // 5;

  /**
   * Creates a TitledBorder instance.
   *
   * @param title  the title the border should display
   */
  public TitledBorder2(String title) {
    this(null, title, LEADING, DEFAULT_POSITION, null, null);
  }

  /**
   * Creates a TitledBorder instance with the specified border
   * and an empty title.
   *
   * @param border  the border
   */
  public TitledBorder2(Border border) {
    this(border, "", LEADING, DEFAULT_POSITION, null, null);
  }

  /**
   * Creates a TitledBorder instance with the specified border
   * and title.
   *
   * @param border  the border
   * @param title  the title the border should display
   */
  public TitledBorder2(Border border, String title) {
    this(border, title, LEADING, DEFAULT_POSITION, null, null);
  }

  /**
   * Creates a TitledBorder instance with the specified border,
   * title, title-justification, and title-position.
   *
   * @param border  the border
   * @param title  the title the border should display
   * @param titleJustification  the justification for the title
   * @param titlePosition  the position for the title
   */
  public TitledBorder2(Border border, String title, int titleJustification, int titlePosition) {
    this(border, title, titleJustification, titlePosition, null, null);
  }

  /**
   * Creates a TitledBorder instance with the specified border,
   * title, title-justification, title-position, and title-font.
   *
   * @param border  the border
   * @param title  the title the border should display
   * @param titleJustification  the justification for the title
   * @param titlePosition  the position for the title
   * @param titleFont  the font for rendering the title
   */
  public TitledBorder2(Border border, String title, int titleJustification, int titlePosition, Font titleFont) {
    this(border, title, titleJustification, titlePosition, titleFont, null);
  }

  /**
   * Creates a TitledBorder instance with the specified border,
   * title, title-justification, title-position, title-font, and
   * title-color.
   *
   * @param border  the border
   * @param title  the title the border should display
   * @param titleJustification  the justification for the title
   * @param titlePosition  the position for the title
   * @param titleFont  the font of the title
   * @param titleColor  the color of the title
   */
  // @ConstructorProperties({"border", "title", "titleJustification", "titlePosition", "titleFont", "titleColor"})
  public TitledBorder2(Border border, String title, int titleJustification,
                       int titlePosition, Font titleFont, Color titleColor) {
    super();
    this.title = title;
    this.border = border;
    this.titleFont = titleFont;
    this.titleColor = titleColor;

    setTitleJustification(titleJustification);
    setTitlePosition(titlePosition);

    this.label = new JLabel();
    this.label.setOpaque(false);
    this.label.putClientProperty(BasicHTML.propertyKey, null);
  }

  /**
   * Paints the border for the specified component with the
   * specified position and size.
   * @param c  the component for which this border is being painted
   * @param g  the paint graphics
   * @param x  the x position of the painted border
   * @param y  the y position of the painted border
   * @param width  the width of the painted border
   * @param height  the height of the painted border
   */
  @SuppressWarnings({"PMD.NPathComplexity", "PMD.ExcessiveMethodLength", "PMD.ConfusingTernary"})
  @Override public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
    Border b = getBorder();
    String str = getTitle();
    if (Objects.nonNull(str) && !str.isEmpty()) {
      int edge = b instanceof TitledBorder2 ? 0 : EDGE_SPACING;
      Dimension size = getLabel(c).getPreferredSize();
      Insets insets = makeBorderInsets(b, c, new Insets(0, 0, 0, 0));

      int bdrX = x + edge;
      int bdrY = y + edge;
      int bdrW = width - edge - edge;
      int bdrH = height - edge - edge;

      int lblY = y;
      int lblH = size.height;
      int position = getPosition();
      switch (position) {
        case ABOVE_TOP:
          insets.left = 0;
          insets.right = 0;
          bdrY += lblH - edge;
          bdrH -= lblH - edge;
          break;
        case TOP:
          insets.top = edge + insets.top / 2 - lblH / 2;
          if (insets.top < edge) {
            bdrY -= insets.top;
            bdrH += insets.top;
          } else {
            lblY += insets.top;
          }
          break;
        case BELOW_TOP:
          lblY += insets.top + edge;
          break;
        case ABOVE_BOTTOM:
          lblY += height - lblH - insets.bottom - edge;
          break;
        case BOTTOM:
          lblY += height - lblH;
          insets.bottom = edge + (insets.bottom - lblH) / 2;
          if (insets.bottom < edge) {
            bdrH += insets.bottom;
          } else {
            lblY -= insets.bottom;
          }
          break;
        case BELOW_BOTTOM:
          insets.left = 0;
          insets.right = 0;
          lblY += height - lblH;
          bdrH -= lblH - edge;
          break;
        default:
          // will NOT execute because of the line preceding the switch.
      }
      insets.left += edge + TEXT_INSET_H;
      insets.right += edge + TEXT_INSET_H;

      int lblX = x;
      int lblW = width - insets.left - insets.right;
      if (lblW > size.width) {
        lblW = size.width;
      }
      switch (getJustification(c)) {
        case LEFT:
          lblX += insets.left;
          break;
        case RIGHT:
          lblX += width - insets.right - lblW;
          break;
        case CENTER:
          lblX += (width - lblW) / 2;
          break;
        default:
          // will NOT execute because of the line preceding the switch.
      }

      if (Objects.nonNull(border)) {
        if (position != TOP && position != BOTTOM) {
          border.paintBorder(c, g, bdrX, bdrY, bdrW, bdrH);
        } else {
          int txsp = TEXT_SPACING;
          Path2D p = new Path2D.Float();
          p.append(new Rectangle(bdrX, bdrY, bdrW, lblY - bdrY), false);
          p.append(new Rectangle(bdrX, lblY, lblX - bdrX - TEXT_SPACING, lblH), false);
          p.append(new Rectangle(lblX + lblW + txsp, lblY, bdrX - lblX + bdrW - lblW - txsp, lblH), false);
          p.append(new Rectangle(bdrX, lblY + lblH, bdrW, bdrY - lblY + bdrH - lblH), false);

          Graphics2D g2 = (Graphics2D) g.create();
          g2.clip(p);
          border.paintBorder(c, g2, bdrX, bdrY, bdrW, bdrH);
          g2.dispose();
        }
      }
      g.translate(lblX, lblY);
      label.setSize(lblW, lblH);
      label.paint(g);
      g.translate(-lblX, -lblY);
    } else if (Objects.nonNull(border)) {
      border.paintBorder(c, g, x, y, width, height);
    }
  }

  /**
   * Reinitialize the insets parameter with this Border's current Insets.
   * @param c  the component for which this border insets value applies
   * @param insets  the object to be reinitialized
   */
  @SuppressWarnings("PMD.AvoidReassigningParameters")
  @Override public Insets getBorderInsets(Component c, Insets insets) {
    Border b = getBorder();
    insets = makeBorderInsets(b, c, insets);

    String str = getTitle();
    if (Objects.nonNull(str) && !str.isEmpty()) {
      int edge = b instanceof TitledBorder2 ? 0 : EDGE_SPACING;
      Dimension size = getLabel(c).getPreferredSize();

      switch (getPosition()) {
        case ABOVE_TOP:
          insets.top += size.height - edge;
          break;
        case TOP:
          if (insets.top < size.height) {
            insets.top = size.height - edge;
          }
          break;
        case BELOW_TOP:
          insets.top += size.height;
          break;
        case ABOVE_BOTTOM:
          insets.bottom += size.height;
          break;
        case BOTTOM:
          if (insets.bottom < size.height) {
            insets.bottom = size.height - edge;
          }
          break;
        case BELOW_BOTTOM:
          insets.bottom += size.height - edge;
          break;
        default:
          // will NOT execute because of the line preceding the switch.
      }
      insets.top += edge + TEXT_SPACING;
      insets.left += edge + TEXT_SPACING;
      insets.right += edge + TEXT_SPACING;
      insets.bottom += edge + TEXT_SPACING;
    }
    return insets;
  }

  /**
   * Returns whether or not the border is opaque.
   */
  @Override public boolean isBorderOpaque() {
    return false;
  }

  /**
   * Returns the title of the titled border.
   *
   * @return the title of the titled border
   */
  public String getTitle() {
    return title;
  }

  /**
   * Returns the border of the titled border.
   *
   * @return the border of the titled border
   */
  public Border getBorder() {
    return Objects.nonNull(border) ? border : UIManager.getBorder("TitledBorder.border");
  }

  /**
   * Returns the title-position of the titled border.
   *
   * @return the title-position of the titled border
   */
  public int getTitlePosition() {
    return titlePosition;
  }

  /**
   * Returns the title-justification of the titled border.
   *
   * @return the title-justification of the titled border
   */
  public int getTitleJustification() {
    return titleJustification;
  }

  /**
   * Returns the title-font of the titled border.
   *
   * @return the title-font of the titled border
   */
  public Font getTitleFont() {
    return Objects.isNull(titleFont) ? UIManager.getFont("TitledBorder.font") : titleFont;
  }

  /**
   * Returns the title-color of the titled border.
   *
   * @return the title-color of the titled border
   */
  public Color getTitleColor() {
    return Objects.isNull(titleColor) ? UIManager.getColor("TitledBorder.titleColor") : titleColor;
  }

  // REMIND(aim): remove all or some of these set methods?

  /**
   * Sets the title of the titled border.
   * @param title  the title for the border
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Sets the border of the titled border.
   * @param border  the border
   */
  public void setBorder(Border border) {
    this.border = border;
  }

  /**
   * Sets the title-position of the titled border.
   * @param titlePosition  the position for the border
   */
  public void setTitlePosition(int titlePosition) {
    switch (titlePosition) {
      case ABOVE_TOP:
      case TOP:
      case BELOW_TOP:
      case ABOVE_BOTTOM:
      case BOTTOM:
      case BELOW_BOTTOM:
      case DEFAULT_POSITION:
        this.titlePosition = titlePosition;
        break;
      default:
        throw new IllegalArgumentException(titlePosition + " is not a valid title position.");
    }
  }

  /**
   * Sets the title-justification of the titled border.
   * @param titleJustification  the justification for the border
   */
  public void setTitleJustification(int titleJustification) {
    switch (titleJustification) {
      case DEFAULT_JUSTIFICATION:
      case LEFT:
      case CENTER:
      case RIGHT:
      case LEADING:
      case TRAILING:
        this.titleJustification = titleJustification;
        break;
      default:
        throw new IllegalArgumentException(titleJustification + " is not a valid title justification.");
    }
  }

  /**
   * Sets the title-font of the titled border.
   * @param titleFont  the font for the border title
   */
  public void setTitleFont(Font titleFont) {
    this.titleFont = titleFont;
  }

  /**
   * Sets the title-color of the titled border.
   * @param titleColor  the color for the border title
   */
  public void setTitleColor(Color titleColor) {
    this.titleColor = titleColor;
  }

  /**
   * Returns the minimum dimensions this border requires
   * in order to fully display the border and title.
   * @param c  the component where this border will be drawn
   * @return the {@code Dimension} object
   */
  @SuppressWarnings("PMD.ConfusingTernary")
  public Dimension getMinimumSize(Component c) {
    Insets insets = getBorderInsets(c);
    Dimension minSize = new Dimension(insets.right + insets.left, insets.top + insets.bottom);
    String str = getTitle();
    if (Objects.nonNull(str) && !str.isEmpty()) {
      Dimension size = getLabel(c).getPreferredSize();

      int position = getPosition();
      if (position != ABOVE_TOP && position != BELOW_BOTTOM) {
        minSize.width += size.width;
      } else if (minSize.width < size.width) {
        minSize.width += size.width;
      }
    }
    return minSize;
  }

  /**
   * Returns the baseline.
   *
   * @throws NullPointerException {@inheritDoc}
   * @throws IllegalArgumentException {@inheritDoc}
   * @see javax.swing.JComponent#getBaseline(int, int)
   * @since 1.6
   */
  @Override public int getBaseline(Component c, int width, int height) {
    Objects.requireNonNull(c, "Must supply non-null component");
    if (width < 0) {
      throw new IllegalArgumentException("Width must be >= 0");
    }
    if (height < 0) {
      throw new IllegalArgumentException("Height must be >= 0");
    }
    Border b = getBorder();
    String str = getTitle();
    if (Objects.nonNull(str) && !str.isEmpty()) {
      int edge = b instanceof TitledBorder2 ? 0 : EDGE_SPACING;
      Dimension size = getLabel(c).getPreferredSize();
      Insets ins = makeBorderInsets(b, c, new Insets(0, 0, 0, 0));

      int baseline = getLabel(c).getBaseline(size.width, size.height);
      switch (getPosition()) {
        case ABOVE_TOP:
          return baseline;
        case TOP:
          ins.top = edge + (ins.top - size.height) / 2;
          return ins.top < edge ? baseline : baseline + ins.top;
        case BELOW_TOP:
          return baseline + ins.top + edge;
        case ABOVE_BOTTOM:
          return baseline + height - size.height - ins.bottom - edge;
        case BOTTOM:
          ins.bottom = edge + (ins.bottom - size.height) / 2;
          return ins.bottom < edge ? baseline + height - size.height : baseline + height - size.height + ins.bottom;
        case BELOW_BOTTOM:
          return baseline + height - size.height;
        default:
          // will NOT execute because of the line preceding the switch.
      }
    }
    return -1;
  }

  /**
   * Returns an enum indicating how the baseline of the border
   * changes as the size changes.
   *
   * @throws NullPointerException {@inheritDoc}
   * @see javax.swing.JComponent#getBaseline(int, int)
   * @since 1.6
   */
  @Override public Component.BaselineResizeBehavior getBaselineResizeBehavior(Component c) {
    super.getBaselineResizeBehavior(c);
    switch (getPosition()) {
      case TitledBorder2.ABOVE_TOP:
      case TitledBorder2.TOP:
      case TitledBorder2.BELOW_TOP:
        return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
      case TitledBorder2.ABOVE_BOTTOM:
      case TitledBorder2.BOTTOM:
      case TitledBorder2.BELOW_BOTTOM:
        return JComponent.BaselineResizeBehavior.CONSTANT_DESCENT;
      default:
        return Component.BaselineResizeBehavior.OTHER;
    }
  }

  private int getPosition() {
    int position = getTitlePosition();
    if (position != DEFAULT_POSITION) {
      return position;
    }
    Object value = UIManager.get("TitledBorder.position");
    if (value instanceof Integer) {
      int i = (Integer) value;
      if (0 < i && i <= 6) {
        return i;
      }
    } else if (value instanceof String) {
      String s = Objects.toString(value);
      if ("ABOVE_TOP".equalsIgnoreCase(s)) {
        return ABOVE_TOP;
      }
      if ("TOP".equalsIgnoreCase(s)) {
        return TOP;
      }
      if ("BELOW_TOP".equalsIgnoreCase(s)) {
        return BELOW_TOP;
      }
      if ("ABOVE_BOTTOM".equalsIgnoreCase(s)) {
        return ABOVE_BOTTOM;
      }
      if ("BOTTOM".equalsIgnoreCase(s)) {
        return BOTTOM;
      }
      if ("BELOW_BOTTOM".equalsIgnoreCase(s)) {
        return BELOW_BOTTOM;
      }
    }
    return TOP;
  }

  private int getJustification(Component c) {
    int justification = getTitleJustification();
    if (justification == LEADING || justification == DEFAULT_JUSTIFICATION) {
      return c.getComponentOrientation().isLeftToRight() ? LEFT : RIGHT;
    }
    if (justification == TRAILING) {
      return c.getComponentOrientation().isLeftToRight() ? RIGHT : LEFT;
    }
    return justification;
  }

  protected final Font getFont(Component c) {
    Font font = getTitleFont();
    if (Objects.nonNull(font)) {
      return font;
    }
    if (Objects.nonNull(c)) {
      font = c.getFont();
      if (Objects.nonNull(font)) {
        return font;
      }
    }
    return new Font(Font.DIALOG, Font.PLAIN, 12);
  }

  private Color getColor(Component c) {
    Color color = getTitleColor();
    if (Objects.nonNull(color)) {
      return color;
    }
    return Objects.nonNull(c) ? c.getForeground() : null;
  }

  private JLabel getLabel(Component c) {
    this.label.setText(getTitle());
    this.label.setFont(getFont(c));
    this.label.setForeground(getColor(c));
    this.label.setComponentOrientation(c.getComponentOrientation());
    this.label.setEnabled(c.isEnabled());
    return this.label;
  }

  // Checkstyle False Positive: OverloadMethodsDeclarationOrder
  // private static Insets getBorderInsets(Border border, Component c, Insets insets) {
  @SuppressWarnings("PMD.AvoidReassigningParameters")
  private static Insets makeBorderInsets(Border border, Component c, Insets insets) {
    if (Objects.isNull(border)) {
      insets.set(0, 0, 0, 0);
    } else if (border instanceof AbstractBorder) {
      AbstractBorder ab = (AbstractBorder) border;
      insets = ab.getBorderInsets(c, insets);
    } else {
      Insets i = border.getBorderInsets(c);
      insets.set(i.top, i.left, i.bottom, i.right);
    }
    return insets;
  }
}
