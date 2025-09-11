import React from 'react';
import { Grid, GridProps } from '@mui/material';

interface ResponsiveGridProps extends Omit<GridProps, 'container' | 'item'> {
  children: React.ReactNode;
  itemSizes?: {
    xs?: number;
    sm?: number;
    md?: number;
    lg?: number;
    xl?: number;
  };
  spacing?: number;
}

const ResponsiveGrid: React.FC<ResponsiveGridProps> = ({
  children,
  itemSizes = { xs: 12, sm: 6, md: 4, lg: 3 },
  spacing = 3,
  ...props
}) => {
  return (
    <Grid container spacing={spacing} {...props}>
      {React.Children.map(children, (child, index) => (
        <Grid
          item
          xs={itemSizes.xs}
          sm={itemSizes.sm}
          md={itemSizes.md}
          lg={itemSizes.lg}
          xl={itemSizes.xl}
          key={index}
        >
          {child}
        </Grid>
      ))}
    </Grid>
  );
};

export default ResponsiveGrid;
