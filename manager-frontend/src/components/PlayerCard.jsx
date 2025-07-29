import React from 'react';

const PlayerCard = ({ player, onEdit, onDelete }) => {
  return (
    <div className="card">
      <h3>{player.first_name} {player.last_name}</h3>
      <p><strong>Age:</strong> {player.age} years</p>
      <p><strong>Experience:</strong> {player.months_of_experience} months</p>
      <p><strong>Team:</strong> {player.team_name}</p>
      <div>
        <button className="btn btn-primary" onClick={() => onEdit(player)}>Edit</button>
        <button className="btn btn-danger" onClick={() => onDelete(player.id)}>Delete</button>
      </div>
    </div>
  );
};

export default PlayerCard;
