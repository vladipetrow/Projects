import React from 'react';
import { Button, ButtonProps } from '@mui/material';

interface AccessibleButtonProps extends ButtonProps {
  loading?: boolean;
  loadingText?: string;
  'aria-describedby'?: string;
}

const AccessibleButton: React.FC<AccessibleButtonProps> = ({
  children,
  loading = false,
  loadingText = 'Зареждане...',
  disabled,
  ...props
}) => {
  return (
    <Button
      {...props}
      disabled={disabled || loading}
      aria-disabled={disabled || loading}
      aria-label={loading ? loadingText : props['aria-label']}
      aria-describedby={props['aria-describedby']}
    >
      {loading ? loadingText : children}
    </Button>
  );
};

export default AccessibleButton;
