import { Link } from "react-router-dom";

function NotFound() {
  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100 text-center">
      <h1 className="text-6xl font-bold text-blue-600">404</h1>
      <p className="mt-4 text-xl text-gray-700">
        Oops! The page you're looking for doesn't exist.
      </p>
      <p className="mt-2 text-gray-600">
        It might have been moved, deleted, or you entered the wrong URL.
      </p>
      <Link
        to="/"
        className="mt-6 px-6 py-3 bg-blue-600 text-white rounded-lg shadow-lg hover:bg-blue-700"
      >
        Go to Homepage
      </Link>
    </div>
  );
}

export default NotFound;
