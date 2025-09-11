import React from 'react';
import {
  Snackbar,
  Alert,
  AlertTitle,
  IconButton,
  Slide,
  SlideProps
} from '@mui/material';
import { Close as CloseIcon } from '@mui/icons-material';
import { ToastMessage } from '../hooks/useToast';

interface ToastProps {
  toast: ToastMessage;
  onClose: (id: string) => void;
}

const SlideTransition = (props: SlideProps) => {
  return <Slide {...props} direction="up" />;
};

const Toast: React.FC<ToastProps> = ({ toast, onClose }) => {
  const handleClose = () => {
    onClose(toast.id);
  };

  return (
    <Snackbar
      open={true}
      autoHideDuration={toast.duration}
      onClose={handleClose}
      TransitionComponent={SlideTransition}
      anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
    >
      <Alert
        severity={toast.type}
        onClose={handleClose}
        action={
          <IconButton
            size="small"
            aria-label="close"
            color="inherit"
            onClick={handleClose}
          >
            <CloseIcon fontSize="small" />
          </IconButton>
        }
        sx={{ minWidth: '300px' }}
      >
        {toast.message}
      </Alert>
    </Snackbar>
  );
};

export default Toast;
