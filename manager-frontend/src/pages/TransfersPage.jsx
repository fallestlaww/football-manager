import React, { useState } from 'react';
import { transferPlayer } from '../services/api';
import TransferModal from '../components/TransferModal';

const TransfersPage = () => {
  const [showModal, setShowModal] = useState(false);
  const [message, setMessage] = useState(null);
  const [error, setError] = useState(null);

  const handleTransfer = async (playerId, newTeamId) => {
    try {
      await transferPlayer(playerId, newTeamId);
      setMessage('Transfer completed successfully');
      setError(null);
      setShowModal(false);
    } catch (err) {
      setError('Error during transfer: ' + (err.response?.data?.message || 'Невідома помилка'));
      setMessage(null);
    }
  };

  return (
    <div>
      <h2 className="card-title">Transfers</h2>

      {message && <div className="alert alert-success">{message}</div>}
      {error && <div className="alert alert-error">{error}</div>}

      <div className="card">
        <h3>Make Transfer</h3>
        <p>Click the button below to transfer a player between teams.</p>
        <button
          className="btn btn-warning"
          onClick={() => setShowModal(true)}
        >
          Make transfer
        </button>
      </div>

      {showModal && (
        <TransferModal
          onClose={() => setShowModal(false)}
          onTransfer={handleTransfer}
        />
      )}
    </div>
  );
};

export default TransfersPage;
