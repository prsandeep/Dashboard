import React, { useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { embedDashboard } from '@superset-ui/embedded-sdk';
import { bugzillaApi } from '../services/bugzillaApi'; // Import the separate API service
import '../bugzilla.css';

function OsTicketDashboard() {
  const navigate = useNavigate();
  const dashboardId = '04bf6277-1545-4d42-94c6-f63e10d17242';
  const containerRef = useRef(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [dashboardConfig, setDashboardConfig] = useState(null);

  const handleGoBack = () => {
    navigate(-1); // Go back to previous page
  };

  useEffect(() => {
    const embedSupersetDashboard = async () => {
      try {
        setLoading(true);
        setError(null);

        // Get guest token using the separate API service
        const tokenResponse = await bugzillaApi.getGuestToken(dashboardId);
        const guestToken = tokenResponse.data.token;

        if (!guestToken) {
          throw new Error('Guest token missing in response');
        }

        // Optionally, get dashboard configuration
        try {
          const configResponse = await bugzillaApi.getDashboardConfig(dashboardId);
          setDashboardConfig(configResponse.data);
        } catch (configError) {
          console.warn('Could not fetch dashboard config:', configError);
          // Continue without config - not critical
        }

        // Embed the dashboard
        await embedDashboard({
          id: dashboardId,
          supersetDomain: 'http://10.226.30.123:8088',
          mountPoint: containerRef.current,
          fetchGuestToken: () => guestToken,
          dashboardUiConfig: {
            hideTitle: true,
            hideChartControls: false,
            hideTab: false,
            // You can use dashboardConfig here if needed
            ...(dashboardConfig?.uiConfig || {})
          },
        });

        setLoading(false);
      } catch (err) {
        console.error('Error embedding Superset dashboard:', err);
        
        // Handle different types of errors
        const errorMessage = err.userMessage || err.message || 'Failed to load dashboard';
        setError(errorMessage);
        setLoading(false);
      }
    };

    embedSupersetDashboard();
  }, [dashboardId]);



  return (
    <div className="App">
      {/* Back Button */}
      <div style={{ 
        position: 'absolute', 
        top: 10, 
        left: 10, 
        zIndex: 1000 
      }}>
        <button 
          onClick={handleGoBack}
          style={{
            padding: '8px 15px',
            background: '#6c757d',
            color: 'white',
            border: 'none',
            borderRadius: '4px',
            cursor: 'pointer',
            fontSize: '14px',
            display: 'flex',
            alignItems: 'center',
            gap: '5px'
          }}
        >
          ← Back
        </button>
      </div>

      {loading && (
        <div style={{ 
          position: 'absolute', 
          top: 10, 
          left: 120, 
          zIndex: 1000,
          background: 'rgba(255, 255, 255, 0.9)',
          padding: '10px',
          borderRadius: '4px'
        }}>
          <p style={{ color: '#555', margin: 0 }}>Loading dashboard...</p>
        </div>
      )}
      
      {error && (
        <div style={{ 
          position: 'absolute', 
          top: 10, 
          left: 120, 
          zIndex: 1000,
          background: 'rgba(255, 0, 0, 0.1)',
          padding: '10px',
          borderRadius: '4px',
          border: '1px solid #ff0000'
        }}>
          <p style={{ color: 'red', margin: 0 }}>Error: {error}</p>
        </div>
      )}


      <div id="dashboard-container" ref={containerRef} />
    </div>
  );
}

export default OsTicketDashboard;


// import React, { useEffect, useState, useRef, useCallback } from 'react';
// import axios from 'axios';
// import { embedDashboard } from "@superset-ui/embedded-sdk";
// import '../bugzilla.css';  

// const FullScreenSupersetDashboard = ({ 
//   dashboardId = "04bf6277-1545-4d42-94c6-f63e10d17242",
//   supersetUrl = 'http://10.226.30.123:8088',
//   showHeader = false
// }) => {
//   const [loading, setLoading] = useState(true);
//   const [error, setError] = useState(null);
//   const [dashboardRef, setDashboardRef] = useState(null);
//   const [debugInfo, setDebugInfo] = useState(null);
//   const [isFullscreen, setIsFullscreen] = useState(false);
//   const containerRef = useRef(null);
//   const wrapperRef = useRef(null);
  
//   const supersetApiUrl = `${supersetUrl}/api/v1/security`;
  
//   // Toggle fullscreen mode
//   const toggleFullscreen = () => {
//     if (!document.fullscreenElement) {
//       // Enter fullscreen
//       if (wrapperRef.current.requestFullscreen) {
//         wrapperRef.current.requestFullscreen();
//       } else if (wrapperRef.current.webkitRequestFullscreen) {
//         wrapperRef.current.webkitRequestFullscreen();
//       } else if (wrapperRef.current.msRequestFullscreen) {
//         wrapperRef.current.msRequestFullscreen();
//       }
//       setIsFullscreen(true);
//     } else {
//       // Exit fullscreen
//       if (document.exitFullscreen) {
//         document.exitFullscreen();
//       } else if (document.webkitExitFullscreen) {
//         document.webkitExitFullscreen();
//       } else if (document.msExitFullscreen) {
//         document.msExitFullscreen();
//       }
//       setIsFullscreen(false);
//     }
//   };

//   // Listen for fullscreen change events
//   useEffect(() => {
//     const handleFullscreenChange = () => {
//       setIsFullscreen(!!document.fullscreenElement);
//     };

//     document.addEventListener('fullscreenchange', handleFullscreenChange);
//     document.addEventListener('webkitfullscreenchange', handleFullscreenChange);
//     document.addEventListener('mozfullscreenchange', handleFullscreenChange);
//     document.addEventListener('MSFullscreenChange', handleFullscreenChange);

//     return () => {
//       document.removeEventListener('fullscreenchange', handleFullscreenChange);
//       document.removeEventListener('webkitfullscreenchange', handleFullscreenChange);
//       document.removeEventListener('mozfullscreenchange', handleFullscreenChange);
//       document.removeEventListener('MSFullscreenChange', handleFullscreenChange);
//     };
//   }, []);
  
//   // Enhanced function to get guest token with better error handling
//   const fetchGuestToken = useCallback(async () => {
//     try {
//       setDebugInfo(null);
      
//       // Step 1: Get access token via login
//       const loginBody = {
//         "password": "12345", // Use a secure method to handle passwords
//         "provider": "db",
//         "refresh": true,
//         "username": "guest"
//       };
      
//       console.log('Attempting login to Superset...');
//       const loginResponse = await axios.post(
//         `${supersetApiUrl}/login`, 
//         loginBody,
//         {
//           headers: {
//             "Content-Type": "application/json"
//           }
//         }
//       );
      
//       const accessToken = loginResponse.data.access_token;
      
//       if (!accessToken) {
//         throw new Error('Failed to obtain access token');
//       }
      
//       console.log('Login successful, fetching guest token...');
      
//       // Step 2: Get guest token for embedding
//       const guestTokenBody = {
//         "subject": "guest-user", // This is required in newer versions
//         "resources": [
//           {
//             "type": "dashboard",
//             "id": dashboardId
//           }
//         ],
//         "rls": [], // Row Level Security rules
//         "user": {
//           "username": "guest",
//           "first_name": "Guest",
//           "last_name": "User",
//           "email": "guest@gmail.com" // Some versions require email
//           //  "username": "admin",
//           // "first_name": "admin",
//           // "last_name": "admin",
//           // "email": "admin"
//         }
//       };
      

// //       const guestTokenBody = {
// //   subject: "guest-user",
// //   resources: [
// //     {
// //       type: "dashboard",
// //       id: dashboardId
// //     }
// //   ],
// //   rls: [],
// //   user: {
// //     username: "guest",
// //     first_name: "Guest",
// //     last_name: "User",
// //     email: "guest@gmail.com"
// //   },
// //   datasources: [
// //     {
// //       type: "table",
// //       id: 12, // ✅ this is required!
// //       permissions: ["can_read"]
// //     }
// //   ]
// // };

//       const guestTokenResponse = await axios.post(
//         `${supersetApiUrl}/guest_token/`, 
//         guestTokenBody,
//         {
//           headers: {
//             "Content-Type": "application/json",
//             "Authorization": `Bearer ${accessToken}`
//           }
//         }
//       );
      
//       const guestToken = guestTokenResponse.data.token;
      
//       if (!guestToken) {
//         throw new Error('Failed to obtain guest token - no token in response');
//       }
      
//       console.log('Guest token obtained successfully');
//       return guestToken;
      
//     } catch (err) {
//       console.error('Error fetching guest token:', err);
      
//       // Enhanced error reporting
//       let errorMessage = 'Failed to fetch guest token';
//       let debugDetails = null;
      
//       if (err.response) {
//         // The request was made and the server responded with a status code
//         // that falls out of the range of 2xx
//         errorMessage = `Server responded with ${err.response.status}: ${err.response.statusText}`;
//         debugDetails = {
//           status: err.response.status,
//           statusText: err.response.statusText,
//           data: err.response.data,
//           headers: err.response.headers
//         };
        
//         if (err.response.data && err.response.data.message) {
//           errorMessage += ` - ${err.response.data.message}`;
//         }
//       } else if (err.request) {
//         // The request was made but no response was received
//         errorMessage = 'No response received from server';
//         debugDetails = { request: err.request };
//       } else {
//         // Something happened in setting up the request that triggered an Error
//         errorMessage = err.message;
//       }
      
//       setDebugInfo(debugDetails);
//       throw new Error(errorMessage);
//     }
//   }, [supersetApiUrl, dashboardId]);
  
//   useEffect(() => {
//     const embedDashboardAsync = async () => {
//       if (!containerRef.current || !dashboardId) {
//         setError('Dashboard container or ID not available');
//         return;
//       }
      
//       try {
//         setLoading(true);
//         setError(null);
//         setDebugInfo(null);
        
//         // Validate dashboard ID format (should be UUID)
//         const uuidRegex = /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i;
//         if (!uuidRegex.test(dashboardId)) {
//           throw new Error(`Invalid dashboard ID format: ${dashboardId}. Expected UUID format.`);
//         }
        
//         console.log('Embedding dashboard with ID:', dashboardId);
        
//         // Step 3: Embed the dashboard
//         const dashboard = await embedDashboard({
//           id: dashboardId,
//           supersetDomain: supersetUrl,
//           mountPoint: containerRef.current,
//           fetchGuestToken: fetchGuestToken,
//           dashboardUiConfig: { 
//             hideTitle: true,
//             hideChartControls: false,
//             hideTab: true,
//           }
//         });
        
//         setDashboardRef(dashboard);
//         setLoading(false);
//         console.log('Dashboard embedded successfully');
        
//       } catch (err) {
//         console.error('Error embedding dashboard:', err);
//         setError(err.message || 'Failed to load dashboard');
//         setLoading(false);
//       }
//     };
    
//     embedDashboardAsync();
    
//     // Cleanup function
//     return () => {
//       if (dashboardRef && dashboardRef.unmount) {
//         dashboardRef.unmount();
//       }
//     };
//   }, [dashboardId, supersetUrl, fetchGuestToken]);
  
//   // Token refresh mechanism
//   useEffect(() => {
//     if (!dashboardRef) return;
    
//     const refreshInterval = setInterval(() => {
//       console.log('Refreshing guest token...');
//       // You could implement automatic token refresh here if needed
//       fetchGuestToken().catch(err => {
//         console.error('Failed to refresh token:', err);
//       });
//     }, 30 * 60 * 1000); // Every 30 minutes
    
//     return () => clearInterval(refreshInterval);
//   }, [dashboardRef, fetchGuestToken]);
  
//   const handleRetry = () => {
//     setError(null);
//     setDebugInfo(null);
//     setLoading(true);
//     setDashboardRef(null);
    
//     // Force re-mount of the component
//     const container = containerRef.current;
//     if (container) {
//       // Clear the container
//       while (container.firstChild) {
//         container.removeChild(container.firstChild);
//       }
//     }
    
//     // This will trigger the useEffect to run again
//     setTimeout(() => {
//       if (containerRef.current) {
//         embedDashboard({
//           id: dashboardId,
//           supersetDomain: supersetUrl,
//           mountPoint: containerRef.current,
//           fetchGuestToken: fetchGuestToken,
//           dashboardUiConfig: { 
//             hideTitle: true,
//             hideChartControls: false,
//             hideTab: true,
//           }
//         }).then(dashboard => {
//           setDashboardRef(dashboard);
//           setLoading(false);
//         }).catch(err => {
//           setError(err.message || 'Failed to load dashboard');
//           setLoading(false);
//         });
//       }
//     }, 500);
//   };
  
//   return (
//     <div className="superset-dashboard-wrapper" ref={wrapperRef}>
//       {!isFullscreen && !showHeader && (
//         <button className="fullscreen-button" onClick={toggleFullscreen}>
//           {isFullscreen ? "Exit Fullscreen" : "Fullscreen"}
//         </button>
//       )}
      
//       {loading && (
//         <div className="loading-spinner-container">
//           <div className="loading-spinner"></div>
//           <p>Loading dashboard...</p>
//         </div>
//       )}
      
//       {error && (
//         <div className="error-container">
//           <h3>Error Loading Dashboard</h3>
//           <p>{error}</p>
          
//           {debugInfo && (
//             <details className="debug-details">
//               <summary>Debug Information</summary>
//               <pre>
//                 {JSON.stringify(debugInfo, null, 2)}
//               </pre>
//             </details>
//           )}
          
//           <button onClick={handleRetry} className="retry-button">
//             Retry
//           </button>
//         </div>
//       )}
      
//       <div 
//         ref={containerRef} 
//         id="superset-container"
//         style={{ 
//           visibility: loading ? 'hidden' : 'visible',
//         }}
//       />
//     </div>
//   );
// };

// // Main App component
// function OsTicketDashboard() {
//   return (
//     <div className="app-container">
 
//       <FullScreenSupersetDashboard 
//         dashboardId="04bf6277-1545-4d42-94c6-f63e10d17242"
//         showHeader={false}
//       />
//     </div>
//   );
// }

// export default OsTicketDashboard;