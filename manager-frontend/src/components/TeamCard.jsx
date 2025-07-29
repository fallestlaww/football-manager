import React from 'react';

const TeamCard = ({ team, onEdit, onDelete }) => {
    return (
        <div className="team-card">
            <h3>{team.name}</h3>
            <p><strong>Country:</strong> {team.country}</p>
            <p><strong>Balance:</strong> ${Number(team.balance || 0).toFixed(2)}</p>
            <p><strong>Commission:</strong> {(team.commission_rate || 0).toFixed(3)}%</p>
            <div className="buttons">
                <button className="btn btn-primary" onClick={() => onEdit(team)}>Edit</button>
                <button className="btn btn-danger" onClick={() => onDelete(team.id)}>Delete</button>
            </div>
        </div>
    );
};

export default TeamCard;
