This is a theme created with the official Material Theme Builder, exported to CSS.
However, the CSS export is not perfect, so I had to add a few modifications:

`theme.light.css`

From:

```css
 --md-sys-color-surface: var(--md-sys-color-surface-light);
```

To:

```css
--md-sys-color-surface: var(--md-ref-palette-neutral98);
```

According to [Color System](https://m3.material.io/styles/color/the-color-system/color-roles) surface should be
N-98. In the generated CSS it is N-99.